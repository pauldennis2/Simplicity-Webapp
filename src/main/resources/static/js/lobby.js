var simplicityLobbyApp = angular.module("simplicityLobbyApp", []);

simplicityLobbyApp.controller('lobbyController', function($scope, $http) {
    $scope.alphaUsers = [];
    $scope.bakerUsers = [];
    $scope.charlieUsers = [];
    $scope.deltaUsers = [];
    $scope.mainLobbyUsers = [];
    $scope.raceImg = "../assets/races/no_race.png";
    $scope.alphaStartReady = true;
    $scope.bakerStartReady = true;
    $scope.charlieStartReady = true;
    $scope.deltaStartReady = true;

    $scope.openGames = [];

    var raceId = -1;
    var myUser = {};

    function getMyUser () {
        $http.get("/my-user.json")
        .then(
            function successCallback (response) {
                myUser = response.data;
                console.log(response.data);
            },

            function errorCallback (response) {
                console.log("Could not find my user");
            });
    };

    function updateUser (lobbyNumber) {
        console.log("Trying to update user's lobby");
        $http.post("/change-users-lobby.json", lobbyNumber)
        .then(
            function successCallback (response) {
            },
            function errorCallback (response) {
            });
    };

    function getLobbyUsers () {
        $http.get("/users.json")
        .then(
            function successCallback (response) {
                $scope.alphaUsers = response.data.alphaUsers;
                $scope.bakerUsers = response.data.bakerUsers;
                $scope.charlieUsers = response.data.charlieUsers;
                $scope.deltaUsers = response.data.deltaUsers;
                $scope.mainLobbyUsers = response.data.mainLobbyUsers;
            },

            function errorCallback (response) {
                console.log("Could not find users");
            });
    };

    function getOpenGames () {
        console.log("getting open games");
        $http.get("/open-games.json")
        .then(
            function successCallback (response) {
                $scope.openGames = response.data;
            },

            function errorCallback (response) {
                console.log("Error callback for open-games");
            }
        )
    }

    $scope.joinGame = function (gameId) {
        console.log("Attempting to join game with id = " + gameId);

        var wrapper = {"gameId": gameId, "raceId": raceId};
        $http.post("/join-multiplayer-game.json", wrapper)
        .then(
            function successCallback (response) {
                console.log("Successfully joined game?");
                console.log(response.data);
                window.location.href = "/main.html";
            },

            function errorCallback (response) {
                console.log("Unable to join game for some reason =(");
            }
        )
    }

    $scope.gameStart = function (lobbyNumber) {
        var lobbyName;
        switch (lobbyNumber) {
            case 0:
                lobbyName = "Alpha";
                break;
            case 1:
                lobbyName = "Baker";
                break;
            case 2:
                lobbyName = "Charlie";
                break;
            case 3:
                lobbyName = "Delta";
                break;
        }
        var wrapper = {"lobbyName": lobbyName, "raceId": raceId};
        $http.post("/new-multiplayer-game.json", wrapper)
        .then(
            function successCallback (response) {
                window.location.href = "/main.html";
            },

            function errorCallback (response) {
                console.log("unable to start new game");
            }
        )
    }

    $scope.raceChange = function () {
        if ($scope.race_selection == "cat") {
            $scope.raceImg = "../assets/races/race1.jpg";
            raceId = 0;
        }
        if ($scope.race_selection == "dog") {
            $scope.raceImg = "../assets/races/race2.jpg";
            raceId = 1;
        }
        if ($scope.race_selection == "horse") {
            $scope.raceImg = "../assets/races/race3.jpg";
            raceId = 2;
        }
        if ($scope.race_selection == "snake") {
            $scope.raceImg = "../assets/races/race4.jpg";
            raceId = 3;
        }
    }

    $scope.moveToGroup = function (group) {
        //Todo: double-check this logic. It seems like we're changing their database status before we're sure we have room
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
            //console.log("a = " + alphaIndex + ", b = " + bakerIndex + ", c = " + charlieIndex + ", d = " + deltaIndex)
            //Remove the user from the group they were in
            if (alphaIndex > -1) {
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
            //group.push(myUser);
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
    }

    getLobbyUsers();
    window.setInterval(function(){
        getLobbyUsers();
    }, 1000);

    getOpenGames();
    window.setInterval(function(){
        getOpenGames();
    }, 1000);

});