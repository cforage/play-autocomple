var myAppModule = angular.module('productModule', ['ui.bootstrap','angular-growl']);

myAppModule.directive('priceValidate', function() {
  return {
    require: 'ngModel',
  link: function(scope, elm, attrs, ctrl) {
    elm.on('change', function (evt) {
      console.dir(scope.selected)
      if(scope.selected.pricing.price > scope.selected.pricing.cost) {
        ctrl.$setValidity('cost', true);
        console.log('valid price')
      return scope.selected.pricing.price;

      } else {
        ctrl.$setValidity('cost', false); 
        console.log('invalid price')
      return undefined;

      }
    })
  }
  }
});

myAppModule.config(['growlProvider', function(growlProvider) {
  growlProvider.globalTimeToLive(2000);
}]);

myAppModule.controller('ProductController',
    function($scope, $http, growl) {
      $scope.getProducts=function(searchTerm){
    	var term = parseInt(searchTerm)
    	if(isNaN(term)){
    		// search term is not number
    		console.log(term + " is not number")
    		return;
    	}
        //var url = 'catalog-comma.json';
        var url = '/products/' + term;
        $http.get(url)
        .then(function(res){
        	products = [];
	    for ( i in res.data ) {
	      var row = res.data[i];
	      var product = {};
	      var rowIdStr = row.id + "";
	      row.ids=row.id + ""
	      products.push(row);
	    }
	    $scope.products = products
  });
if(!$scope.products){
  var emptyArray = []; 
  return emptyArray;
}else{
  return $scope.products;
}
}// end getProducts

$scope.updateProduct=function(form){
  if(!form.$valid){
    // prevent invalid submission
    return;
  }
  var prod = {}
  if($scope.selected){
    prod.id = $scope.selected.id;
    prod.title= $scope.selected.title,
      prod.price= $scope.selected.pricing.price
  }
  var data = JSON.stringify(prod);
  console.log('update product ' + data)
    $http({
      url: '/updateProduct',
      method: "POST",
      data: data,
      headers: {'Content-Type': 'application/json'}
    }).success(function (data, status, headers, config) {
      // send success message
      growl.addInfoMessage("product updated")
    }).error(function (data, status, headers, config) {
      // send error message
      growl.addErrorMessage("product update failed")
    });
} // end updateProduct
});

