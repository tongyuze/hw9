var app = angular.module('myApp',["ngAnimate"]);

app.controller('MyCtrl',function ($scope, $http){
      var lat;
      var long;
      var localStorage = window.localStorage;
    $(document).ready(function(){

        $scope.showbar = false;	
        $scope.showdet = false;
        $scope.showbar2 = false;
        $scope.showp = false;
        $scope.count = 0;

    window.fbAsyncInit = function() {
      FB.init({
        appId: '986969244770042',
        status: true, 
        cookie: true, 
        xfbml: true,
        version: 'v2.4'
      });
    };

    (function(d, s, id){
       var js, fjs = d.getElementsByTagName(s)[0];
       if (d.getElementById(id)) {return;}
       js = d.createElement(s); js.id = id;
       js.src = "//connect.facebook.net/en_US/sdk.js";
       fjs.parentNode.insertBefore(js, fjs);
     }(document, 'script', 'facebook-jssdk'));

        var options = {
        	enableHighAccuracy: true,
        	timeout: 5000,
        	maximumAge: 0
      	};

      	function success(pos) {
        		var crd = pos.coords;
        		lat = `${crd.latitude}`;
        		long = `${crd.longitude}`;
      	};

      	function error(err) {
        		console.warn(`ERROR(${err.code}): ${err.message}`);
      	};

      	navigator.geolocation.getCurrentPosition(success, error, options);

        $scope.clean = function(){
            $scope.appear = false;
            $scope.showdet = false;
        }

        /*ajax*/
        $scope.search = function(){
            if($scope.keyword == null)
            {
                alert("this cant be empty");
                return;
            }
            else
            {
                $scope.appear = false;
                $scope.showdet = false;
                $scope.showbar = true;
                $http({
                  method: 'GET',
                  url: '/index.php',
                  dataType: 'json',
                  params:{keyword: $scope.keyword,lat: lat, long: long}
                }).then(function successCallback(response) {
                  $scope.showbar = false;
                  $scope.appear = true;
                  $scope.users = response.data[0].data;
                  $scope.pages = response.data[1].data;
                  $scope.events = response.data[2].data;
                  $scope.places = response.data[3].data;
                  $scope.groups = response.data[4].data;

                  $scope.usersPager = response.data[0].paging;
                  $scope.pagesPager = response.data[1].paging;
                  $scope.eventsPager = response.data[2].paging;
                  $scope.placesPager = response.data[3].paging;
                  $scope.groupsPager = response.data[4].paging;

                  $scope.getFavoriteList();
                }, function errorCallback(response){
                  alert("try again");
                }); 
              }
        };

        $scope.paging1 = function(url){
            $http({
              method: 'GET',
              url: url,
            }).then(function successCallback(response) {
              $scope.users = response.data.data;
              $scope.usersPager = response.data.paging;
            });
        };

        $scope.paging2 = function(url){
            $http({
              method: 'GET',
              url: url,
            }).then(function successCallback(response) {
              $scope.pages = response.data.data;
              $scope.pagesPager = response.data.paging;
            });
        };

        $scope.paging3 = function(url){
            $http({
              method: 'GET',
              url: url,
            }).then(function successCallback(response) {
              $scope.events = response.data.data;
              $scope.eventsPager = response.data.paging;
            });
        };

        $scope.paging4 = function(url){
            $http({
              method: 'GET',
              url: url,
            }).then(function successCallback(response) {
              $scope.places = response.data.data;
              $scope.placesPager = response.data.paging;
            });
        };

        $scope.paging5 = function(url){
            $http({
              method: 'GET',
              url: url,
            }).then(function successCallback(response) {
              $scope.groups = response.data.data;
              $scope.groupsPager = response.data.paging;
            });
        };

        $scope.getFavoriteList = function(){
            $scope.favoriteList = [];
            for(var i = 0; i<localStorage.length; i++)
            {
                var temp = JSON.parse(localStorage.getItem(localStorage.key(i)));
                $scope.favoriteList.push(temp);
            }
        }

        $scope.fav = function(id, url, name, type){
            if(localStorage.hasOwnProperty(id)){
              localStorage.removeItem(id);
            }
            else{
              var favjson = {"id":id, "url":url, "name":name, "type":type};
              favjson = JSON.stringify(favjson);
              localStorage.setItem(id, favjson);
            }
            $scope.getFavoriteList();
            console.log($scope.favoriteList);
        };

        $scope.light = function(id){
            var exist;
            if(localStorage.hasOwnProperty(id)){
                exist = true;
            }
            else
            {
                exist = false;
            }
            return exist;
        }

        $scope.remove = function(id){
            localStorage.removeItem(id);
            $scope.getFavoriteList();
        }


        $scope.det = function(id,type){
            $scope.appear = false;
            $scope.showdet = true;
            $scope.showbar2 = true;
            $scope.nopost = false;
            $scope.nophoto = false;
            $scope.haveposts = false;
            $scope.havephotos = false;
            $scope.type = type;
            $http({
              method: 'GET',
              url: '/index.php',
              dataType:'json',
              params: {id:id}
            }).then(function suuccessCallback(response){
              $scope.showbar2 = false;
              $scope.showp = true;

              $scope.detaildata = response.data;
              $scope.name = response.data.name;
              $scope.albums = response.data.albums;
              $scope.icon = response.data.picture.url;
              $scope.posts = response.data.posts; 
              if($scope.albums == null)
              {
                  $scope.nophoto = true;
              }
              else
              {
                  $scope.havephotos = true;
              }
              if($scope.posts == null)
              {
                  $scope.nopost = true;
              }
              else
              {
                  $scope.haveposts = true;
              }
            });
        };

        $scope.back = function(){
            $scope.showdet = false;
            $scope.appear = true;
        };

        $scope.change = function(){
            $scope.showdet = false;
            if($scope.keyword != null){
                $scope.appear = true;
            }
        };

        $scope.share = function(){
            FB.ui({
              app_id: '986969244770042',
              method: 'feed',
              display: 'popup',
              href: 'https://developers.facebook.com/docs/',
              link: window.location.href, picture: $scope.icon, name: $scope.name, caption: 'FB SEARCH FROM USC CSCI571',
              }, function(response){
                  if (response && !response.error_message)
                  alert("Posted Successfully");
                  else
                  alert("Not Posted");
              });
        };

    });
});
