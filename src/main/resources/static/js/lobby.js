var simplicityLobbyApp = angular.module("simplicityLobbyApp", []);

console.log("In lobby.js");

simplicityLobbyApp.controller('lobbyController', function($scope, $http) {
    console.log("Lobbycontroller initialized");
    $scope.alphaUsers = [];
    $scope.bakerUsers = [];
    $scope.charlieUsers = [];
    $scope.deltaUsers = [];
    $scope.mainLobbyUsers = [];
    var myUser = {};
    getMyUser = function () {
        console.log("Looking for my user");
        $http.get("/my-user.json")
        .then(
            function successCallback (response) {
                console.log ("Found my user");
                myUser = response.data;
                console.log(response.data);
            },

            function errorCallback (response) {
                console.log("Could not find my user");
            });
    };

    $scope.raceChange = function () {
        if ($scope.race_selection == "cat") {
            $scope.raceImg = "../assets/1.jpg";
        }
        if ($scope.race_selection == "dog") {
            $scope.raceImg = "../assets/2.jpg";
        }
        if ($scope.race_selection == "horse") {
            $scope.raceImg = "../assets/3.jpg";
        }
        if ($scope.race_selection == "snake") {
            $scope.raceImg = "../assets/4.jpg";
        }
    }

    updateUser = function (lobbyNumber) {
        console.log("Trying to update user's lobby");
        $http.post("/change-users-lobby.json", lobbyNumber)
        .then(
            function successCallback (response) {
            },
            function errorCallback (response) {
            });
    };

    getLobbyUsers = function () {
        console.log("Getting lobby users");
        $http.get("/users.json")
        .then(
            function successCallback (response) {
                console.log("Found users");
                console.log(response.data);
                $scope.alphaUsers = response.data.alphaUsers;
                $scope.bakerUsers = response.data.bakerUsers;
                $scope.charlieUsers = response.data.charlieUsers;
                $scope.deltaUsers = response.data.deltaUsers;
                $scope.mainLobbyUsers = response.data.mainLobbyUsers;
            },

            function errorCallback (response) {
            });
    };
    currentUserIndex = 1;
    getLobbyUsers();
        window.setInterval(function(){
          getLobbyUsers();
        }, 5000);

    $scope.raceImg = "../assets/1.jpg";

    $scope.moveToGroup = function (group) {
        if (group === $scope.alphaUsers) {
            updateUser(2); //Third value in the enum LobbyStatus
        }
        if (group === $scope.bakerUsers) {
            updateUser(3);
        }
        if (group === $scope.charlieUsers) {
            updateUser(4);
        }
        if (group === $scope.deltaUsers) {
            updateUser(5);
        }

        var index = group.indexOf(myUser);
        if (index < 0 && group.length < 4) { //If we are NOT in the group and the group is not full
            $scope.bakerStart = false;
            var alphaIndex = $scope.alphaUsers.indexOf(myUser);
            var bakerIndex = $scope.bakerUsers.indexOf(myUser);
            var charlieIndex = $scope.charlieUsers.indexOf(myUser);
            var deltaIndex = $scope.deltaUsers.indexOf(myUser);
            var mainIndex = $scope.mainLobbyUsers.indexOf(myUser);
            console.log("a = " + alphaIndex + ", b = " + bakerIndex + ", c = " + charlieIndex + ", d = " + deltaIndex)
            //Remove the user from the group they were in
            if (alphaIndex > -1) {
                //$scope.alphaUsers.shift();
                $scope.alphaUsers.splice(alphaIndex, 1);

            }
            if (bakerIndex > -1) {
                $scope.bakerUsers.splice(bakerIndex, 1);

            }
            if (charlieIndex > -1) {
                $scope.charlieUsers.splice(charlieIndex, 1);

            }
            if (deltaIndex > -1) {
                $scope.deltaUsers.splice(deltaIndex, 1);

            }
            if (mainIndex > -1) {
                $scope.mainLobbyUsers.splice(mainIndex, 1);
            }
            //Add the user to the selected group
            group.push(myUser);
            //update leader status
            if (myUser.leader) {
                if (alphaIndex > -1) {
                    if ($scope.alphaUsers[0] != null) {
                        $scope.alphaUsers[0].leader = true;
                    }
                }
                if (bakerIndex > -1) {
                    if ($scope.bakerUsers[0] != null) {
                        $scope.bakerUsers[0].leader = true;
                    }
                }
                if (charlieIndex > -1) {
                    if($scope.charlieUsers[0] != null) {
                        $scope.charlieUsers[0].leader = true;
                    }
                }
                if (deltaIndex > -1) {
                    if ($scope.deltaUsers[0] != null) {
                        $scope.deltaUsers[0].leader = true;
                    }
                }
            }
            if (group.length == 1) {
                myUser.leader = true;
            } else {
                myUser.leader = false;
            }
        }
        //group.shift();
        //$scope.deltaUsers.unshift(users[currentUserIndex]);
    }
});