var myAppModule = angular.module('productModule', ['ui.bootstrap']);
myAppModule.controller('ProductController',
    function($scope, $http) {
      var someText = {};
      someText.message = 'Hello Angular';
      $scope.someText = someText;

      $http.get('catalog-comma.json')
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
});

