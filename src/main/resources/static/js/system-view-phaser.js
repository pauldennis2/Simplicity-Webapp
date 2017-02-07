var game = new Phaser.Game(800, 600, Phaser.AUTO, '', { preload: preload, create: create, update: update });

function preload() {
    game.load.image('stars', 'starfield.png');
}

var player;
var platforms;
var cursors;

var stars;

function create() {

    //  A simple background for our game
    game.add.sprite(0, 0, 'stars');

    // The player and its settings
    player = game.add.sprite(32, game.world.height - 150, 'dude');


    //  Finally some stars to collect
    stars = game.add.group();

    //  We will enable physics for any star that is created in this group
    stars.enableBody = true;

    //  Here we'll create 12 of them evenly spaced apart
    for (var i = 0; i < 12; i++)
    {
        //  Create a star inside of the 'stars' group
        var star = stars.create(i * 70, 0, 'star');

        //  This just gives each star a slightly random bounce value
        star.body.bounce.y = 0.7 + Math.random() * 0.2;
    }
}

function update() {
    //Called every frame
}