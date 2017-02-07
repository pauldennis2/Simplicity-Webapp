var spaApp = angular.module("shipyardApp", []);

console.log("spaApp module created");
spaApp.controller('shipyardController', function($scope) {
    console.log("Initializing spaController");

    $scope.shipyard = {};
    $scope.shipyard.name = "Utopia Planitia";
    $scope.shipyard.systemName = "Alpha Centauri";

    $scope.shipSizes = [];
    $scope.shipSizes[0] = "Fighter";
    $scope.shipSizes[1] = "Destroyer";

    $scope.smallWeapons = [];
    $scope.smallWeapons[0] = "Beam (s)";
    $scope.smallWeapons[1] = "Missile (s)";

    $scope.sm1 = true;
    $scope.sm2 = true;
    $scope.sm3 = true;
    $scope.sm4 = true;

    $scope.lg1 = true;
    $scope.lg2 = true;
    $scope.lg3 = true;
    $scope.upg1 = true;
    $scope.upg2 = true;

    $scope.largeWeapons = [];
    $scope.largeWeapons[0] = "Beam (l)";
    $scope.largeWeapons[1] = "Missile (l)";

    $scope.productionQueue = [];
    $scope.productionQueue[0] = {"name":"Starship Enterprise"};
    $scope.productionQueue[1] = {"name":"Starship Voyager"};

    /*Health: {{shipInfo.health}}<br>
    Shield Health: {{shipInfo.shield.shieldHealth}}<br>
    Shield Strength: {{shipInfo.shield.shieldStrength}}<br>
    Shield Regen: {{shipInfo.shield.shieldRegen}}<br>
    Generator Reserve: {{shipInfo.generator.reservePower}}<br>
    Generator Per Turn: {{shipInfo.generator.perTurn}}<br>
    Fighter Berths: {{shipInfo.fighterBerths}}*/

    $scope.shipInfo = {};
    $scope.shipInfo.shield = {};
    $scope.shipInfo.generator = {};

    fighterShield = {};
    fighterShield.shieldHealth = 30;
    fighterShield.shieldStrength = 10;
    fighterShield.shieldRegen = 0;

    destroyerShield = {};
    destroyerShield.shieldHealth = 100;
    destroyerShield.shieldStrength = 12;
    destroyerShield.shieldRegen = 1;

    fighterGenerator = {};
    fighterGenerator.reservePower = 100;
    fighterGenerator.perTurn = 25;

    destroyerGenerator = {};//funny name
    destroyerGenerator.reservePower = 400;
    destroyerGenerator.perTurn = 75;

    $scope.updateInfo = function () {
        console.log ("the ship sizes they are a changin");
        console.log("selected = " + $scope.selectedItem);
        if ($scope.selectedItem === "Fighter") {
            console.log("it's a wee little fighter");
            $scope.sm1 = false;
            $scope.sm2 = false;
            $scope.sm3 = true;
            $scope.sm4 = true;

            $scope.lg1 = true;

            $scope.shipInfo.shield = fighterShield;
            $scope.shipInfo.generator = fighterGenerator;
        } else if ($scope.selectedItem === "Destroyer") {
            console.log("it's a somewhat beefier destroyer");
            $scope.sm1 = false;
            $scope.sm2 = false;
            $scope.sm3 = true;
            $scope.sm4 = true;

            $scope.lg1 = false;
            $scope.shipInfo.shield = destroyerShield;
            $scope.shipInfo.generator = destroyerGenerator;
        } else {
            console.log("I have no idea what it is");
        }
    };
});