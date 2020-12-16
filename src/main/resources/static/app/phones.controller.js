(function(){
    'use strict'

    angular
        .module('app')
        .controller('PhoneController', PhoneController );

    PhoneController.$inject = ['$http'];

    function PhoneController($http){
        var vm = this;

        vm.phones = [];
        vm.listAllPhones = listAllPhones;
        vm.listFilterPhones = listFilterPhones;
        vm.removePhone = removePhone;


        init();

        function init(){
            listAllPhones();

        }

        function listAllPhones(){
            var url="all";
            var phonesPromise = $http.get(url);
            phonesPromise.then(function(response){
                vm.phones = response.data

            });

        }

        function listFilterPhones(){
            var price = document.getElementById("affordablePriceInput").value;
            var priceWarning = document.getElementById("priceWarning");
            var regex = /^[0-9]+\.?[0-9]*$/i;

            if(!price.replace(/\s/g, '').length){
                priceWarning.innerHTML = "Please introduce a price!";
                return false;
            }else{
                priceWarning.innerHTML = null;

            }

            if(price <= 0){
                priceWarning.innerHTML = "Please introduce a price higher than 0!";
                return false;

            }else{
                priceWarning.innerHTML = null;

            }

            if(!regex.test(price)){
                priceWarning.innerHTML = "Please introduce a valid price!";
                return false;

            }else{
                priceWarning.innerHTML = null;

            }

            var url="lessThan/" + price;
            var phonesPromise = $http.get(url);
            phonesPromise.then(function(response){
                vm.phones = response.data

            });

        }

        function removePhone(id){
            var url="remove/"+id;
            $http.delete(url).then(function(response){
                vm.phones = response.data;

            });

        }

    }

})();