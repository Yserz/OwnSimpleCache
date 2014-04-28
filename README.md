# OwnSimpleCache
OwnSimpleCache is a fast and lightweight object cache for Java EE 6+ projects.
OwnSimpleCache provides easy to use annotations for caching and clearing objects.
Cached objects will be deleted out of the cache if they expire but it's not guaranteed it happens instantly.
This way OwnSimpleCache does not need threads!
In addition OwnSimpleCache provides support for JAX-RS Entity-Tags. 
Combining these two caching technologies results in faster responses and less bandwidth consumption.

## Download
You can download TabSwitch from <a href="https://bitbucket.org/api/1.0/repositories/Yserz/ownmavenrepo/raw/HEAD/de/yser/OwnSimpleCache/0.1/OwnSimpleCache-0.1.jar" type="application/octet-stream">here</a>.

## Using Maven
If you want use Maven simply add the repository and dependency of OwnSimpleCache
<code>
...
<repositories>
    ...
    <repository>
        <id>maven-own-repo</id>
        <name>Own Repository</name>
        <url>https://bitbucket.org/api/1.0/repositories/Yserz/ownmavenrepo/raw/HEAD/</url>
    </repository>
    ...
</repositories>
...
<dependencies>
    ...
    <dependency>
        <groupId>de.yser</groupId>
        <artifactId>OwnSimpleCache</artifactId>
        <version>0.1</version>
    </dependency>
    ...
<dependencies>
...
</code>

## Issue Management
If you find a bug or have an idea for a feature, feel free to post it [here](https://github.com/Yserz/OwnSimpleCache/issues).

## Making a Release
- [Maven Release Plugin: The Final Nail in the Coffin](http://axelfontaine.com/blog/final-nail.html)

1. Checking the software out: <code>git clone</code>
2. Giving it a version: <code>mvn versions:set</code>
3. Building, testing, packaging and deploying it to an artifact repository: <code>mvn deploy</code>
4. Tagging this state in SCM: <code>mvn scm:tag</code>

## Sources

### Maven Release Sources
- [Maven Release Plugin: The Final Nail in the Coffin](http://axelfontaine.com/blog/final-nail.html)

### License Sources
- [Open-Source-Lizenzen](http://www.heise.de/open/artikel/Open-Source-Lizenzen-221957.html)

### Markdown Sources
- [Markdown: Syntax](http://daringfireball.net/projects/markdown/syntax)
- [HTML Codes](http://character-code.com/arrows-html-codes.php)
