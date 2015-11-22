# FloreantPOS

<img src="http://floreant.org/wp-content/uploads/2014/12/pos_terminal-1030x1010-1030x1010.png" width="400px"/>

A free, open-source point-of-sale application for restaurants (and potentially more)

Licensed under the [MRPL](http://www.floreantpos.org/license.html) (a modified MPL), FloreantPOS is a
java-based multi-terminal "point of sale" system intended for restaurants.  It uses an [Apache derby](https://db.apache.org/derby/),
[mysql/mariadb](https://mariadb.org/), or [postgresql](https://postgresql.org) back end database.  Your choice.

FloreantPOS has also been (experimentally) known to run as a web app in a browser when paired with [webswing](http://webswing.org/#!/home).  I posted a somewhat
old set of instructions for trying Floreant as a web app [here](https://fat-tire.github.io/floreantpos.html).

Much of FloreantPOS's development code is currently hosted at [sourceforge.net](http://sourceforge.net/projects/floreantpos/) using [subversion](https://subversion.apache.org/), but I am hoping that the lead developers come to their senses and switch to git soon.  But until they do, this github repository
is just a mirror and may not contain the very latest updates.

If you'd like to automatically build FloreantPOS from the original svn code, I have written a [shell script](https://en.wikipedia.org/wiki/Bash_(Unix_shell)) to do so (in Ubuntu Linux), which
I've made available under the GPL called [floreantpos_updater](https://github.com/fat-tire/floreantpos_updater).  This can automatically install build dependencies, update the source code from Sourceforge, and build into a .zip as well as an "active" directory which could be used to run floreant.

Or, if you'd like to build with eclipse or netbean, you can find instructions [here](http://floreant.org/support/development/#tab-1436629735676-3-1) on the Floreant Web site.

Floreant's main web site is [here](http://floreant.org)
Their main forum is [here](http://forum.floreantpos.org/forumdisplay.php?2-Main-Forum).

Floreant's lead developers are at [OROCUBE LLC](https://orocube.com/), and they offer additional plugins for Floreant, which I believe are currently (11/21/15) a free download, though closed-source.

Finally-- with respect to branches:

* `master` -- this is the original code from the sourceforge svn repository.  It may not be fully up-to-date as it must be manually synced with upstream's svn repository.
* `fattire-master` -- This contains my additions, such as the LICENSE file as well as this README.md and anything else I may feel like adding.  See the commit log for details.
