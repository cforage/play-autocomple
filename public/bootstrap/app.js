var myAppModule = angular.module('productModule', ['ui.bootstrap','angular-growl']);

myAppModule.directive('priceValidate', function() {
return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
    	elm.on('blur', function (evt) {
    		console.dir(scope.selected)
    	if(scope.selected.pricing.price >= scope.selected.pricing.cost) {
            ctrl.$setValidity('cost', true);
            return scope.selected.pricing.price;
            console.log('valid price')
        } else {
            ctrl.$setValidity('cost', false);                    
            return undefined;
            console.log('invalid price')
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
    
	
	$scope.getProducts=function(term){
        //var url = 'catalog-comma.json';
        var url = '/products/' + term;
        $http.get(url)
        .then(function(res){
        products = [];
        for ( i in res.data ) {
          var row = res.data[i];
          var product ={};
          var rowIdStr = row.id + "";
          row.ids=row.id+""
          products.push(row);
        }
        $scope.products = products
      });
      if(!$scope.products){
        var v = []; 
        return  v;
      }else{
        return $scope.products;
      }
    }// end getProducts
      
  $scope.updateProduct=function(form){
	if(!form.$valid){
		return;
	}
  	var prod = {}
  	if($scope.selected){
  		prod.id = $scope.selected.id;
  		prod.title= $scope.selected.title,
  		prod.price= $scope.selected.pricing.price
  	}
  	var dt = JSON.stringify(prod);
  	console.log('update product ' + dt)
  	$http({
          url: '/updateProduct',
          method: "POST",
          data: dt,
          headers: {'Content-Type': 'application/json'}
      }).success(function (data, status, headers, config) {
    	  		growl.addInfoMessage("product updated")
              $scope.updated = true; // assign  $scope.persons here as promise is resolved here 
          }).error(function (data, status, headers, config) {
              $scope.updated = false;
              growl.addErrorMessage("product update failed")
          });
  }
      
});

