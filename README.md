# Awesome Space Game

A simple Java project demonstrating how to make a
game with Java's built-in graphics features.

Currently, the game has relatively smooth 60 fps
graphics, sprites stored as transparent PNG and
text loaded through external TTF file.

The actual graphics are drawn directly into a
JFrame's graphics context - not the cleanest
approach, but not the ugliest, either.

It'll be interesting to see if this works on Windwows.


## Compiling and running

In the project directory, do

```
# Create a directory for class files
mkdir -p bin

# Compile the project, specifying 'bin' as the target directory
# for the generated class files and placing 'src' in the class path
javac -d bin -cp src src/SpaceGame.java

# Run the game, specifying bin as the place to look for
# class files
java -cp bin SpaceGame
```

## Licence

MIT or WTFPL, whichever you prefer to use.

All code was put together by me in less than five hours;
all graphics have been yoinked from OpenGameArt.org and
the font was procured from dafont.com
