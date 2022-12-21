# Team Bool

# Team Members

Last Name       | First Name      | GitHub User Name
--------------- | --------------- | --------------------
Murphy             | Michael             | ml-murphy
Thomas             | Joshua             | Keajoj


# Test Results
How many of the dumpfiles matched (using the check-dumpfiles.sh script)?
The dumpfiles did not necessarily have data written in the same order, 
but the sequences and frequencies matched between them.

How many of the query files results matched (using the check-queries.sh script)?
All of them!

# Cache Performance Results

How much did a Cache of size 100 improve your performance compared to no cache?
Using test0.gbk, auto-degree, subsequence length 12, cache size 100:
13942
Using test0.gbk, auto-degree, subsequence length 12, no cache: 
13223

How much did a Cache of size 500 improve your performance compared to no cache?
Using test0.gbk, auto-degree, subsequence length 12, cache size 500:
12829
Using test0.gbk, auto-degree, subsequence length 12, no cache: 
13223

## Cache Performance Data
Using the included testing scripts, we tested B-Tree creation on two different
GeneBank files: test0.gbk & test5.gbk. We ran B-Tree creation on each of these
files with no cache, a cache of size 100, and a cache of size 500. Afterward,
we compared results and saw that a cache of size 100 offered a substantial
performance improvement over using no cache at all. The performance improvement
of cache size 500 versus 100 was less dramatic but was still notable. The full
comparison data is reproduced below.

test0_create_noCache | test0_create_cache100
---------------------|-------------------------------
Operation concluded successfully in 54 ms                       |       Operation concluded successfully in 52 ms
Operation concluded successfully in 123 ms                      |       Operation concluded successfully in 57 ms
Operation concluded successfully in 140 ms                      |       Operation concluded successfully in 64 ms
Operation concluded successfully in 122 ms                      |       Operation concluded successfully in 76 ms
Operation concluded successfully in 133 ms                      |       Operation concluded successfully in 90 ms
Operation concluded successfully in 163 ms                      |       Operation concluded successfully in 97 ms
Operation concluded successfully in 145 ms                      |       Operation concluded successfully in 97 ms
Operation concluded successfully in 144 ms                      |       Operation concluded successfully in 99 ms
Operation concluded successfully in 159 ms                      |       Operation concluded successfully in 111 ms
Operation concluded successfully in 153 ms                      |       Operation concluded successfully in 115 ms

test0_create_cache100 | test0_create_cache500
---------------------|-------------------------------
Operation concluded successfully in 52 ms                       |       Operation concluded successfully in 50 ms
Operation concluded successfully in 51 ms                       |       Operation concluded successfully in 57 ms
Operation concluded successfully in 52 ms                       |       Operation concluded successfully in 48 ms
Operation concluded successfully in 57 ms                       |       Operation concluded successfully in 58 ms
Operation concluded successfully in 64 ms                       |       Operation concluded successfully in 66 ms
Operation concluded successfully in 76 ms                       |       Operation concluded successfully in 85 ms
Operation concluded successfully in 90 ms                       |       Operation concluded successfully in 94 ms
Operation concluded successfully in 97 ms                       |       Operation concluded successfully in 96 ms
Operation concluded successfully in 97 ms                       |       Operation concluded successfully in 100 ms
Operation concluded successfully in 99 ms                       |       Operation concluded successfully in 100 ms
Operation concluded successfully in 111 ms                      |       Operation concluded successfully in 101 ms
Operation concluded successfully in 115 ms                      |       Operation concluded successfully in 122 ms

test5_create_noCache | test5_create_cache100
---------------------|-------------------------------
Operation concluded successfully in 8743 ms                     |       Operation concluded successfully in 8490 ms
Operation concluded successfully in 4956 ms                     |       Operation concluded successfully in 9937 ms
Operation concluded successfully in 10699 ms                    |       Operation concluded successfully in 5288 ms
Operation concluded successfully in 29539 ms                    |       Operation concluded successfully in 7828 ms
Operation concluded successfully in 25809 ms                    |       Operation concluded successfully in 8207 ms
Operation concluded successfully in 27333 ms                    |       Operation concluded successfully in 8674 ms
Operation concluded successfully in 28803 ms                    |       Operation concluded successfully in 9814 ms
Operation concluded successfully in 29252 ms                    |       Operation concluded successfully in 14781 ms
Operation concluded successfully in 33255 ms                    |       Operation concluded successfully in 17307 ms
Operation concluded successfully in 35403 ms                    |       Operation concluded successfully in 17551 ms
Operation concluded successfully in 44476 ms                    |       Operation concluded successfully in 30306 ms
Operation concluded successfully in 50001 ms                    |       Operation concluded successfully in 36598 ms

test5_create_cache100 | test5_create_cache500
---------------------|-------------------------------
Operation concluded successfully in 8490 ms                     |       Operation concluded successfully in 4943 ms
Operation concluded successfully in 9937 ms                     |       Operation concluded successfully in 6698 ms
Operation concluded successfully in 5288 ms                     |       Operation concluded successfully in 7140 ms
Operation concluded successfully in 7828 ms                     |       Operation concluded successfully in 8010 ms
Operation concluded successfully in 8207 ms                     |       Operation concluded successfully in 8264 ms
Operation concluded successfully in 8674 ms                     |       Operation concluded successfully in 8512 ms
Operation concluded successfully in 9814 ms                     |       Operation concluded successfully in 8739 ms
Operation concluded successfully in 14781 ms                    |       Operation concluded successfully in 7912 ms
Operation concluded successfully in 17307 ms                    |       Operation concluded successfully in 13189 ms
Operation concluded successfully in 17551 ms                    |       Operation concluded successfully in 15681 ms
Operation concluded successfully in 30306 ms                    |       Operation concluded successfully in 24144 ms
Operation concluded successfully in 36598 ms                    |       Operation concluded successfully in 28905 ms



# BTree Binary File Format and Layout
*<b> BTree binary files are saved to the file:</b>
   - `[gbkFileName].gbk.btree.data.[subsequence length].[degree of tree]`
   
* <b>Each node's data is accessed to/from disk in the following order:</b>
   - <b>long</b>  - address
   - <b>int</b> - # of Keys
   - <b>int</b> - # of Children
   - Block of `i` keys `<frequency of TreeObject[i] key of TreeObject[i]>` for each key held
   - Block of <b>long</b> Child addresses `<address_0> <address_1> ... <address_[# of Children]>` for each child held

<i><u><b>
[ node 0 data ]
[ . ]
[ . ]
[ . ]
[ node [number of nodes] data ]
</i></u></b>
* <b>Metadata is stored in a separate file, with respective BTree file name followed by `.metadata`. </b>
 -<i> e.g. `xyz.gbk.btree.data`.`<k>.<t>`.`metadata` </i>
* <b> MetaData contains the following serialized variables: </b>
 -int degree
 -int numNodes
 -int debugLevel
 -int nodeSize
 -int address of root
 

 
 


# Reflection

## Reflection (Team member name: Michael Murphy)
This project felt like the first "big project" we've worked on, and it felt accomplishing finishing it. Getting the boiler plate code was the main challenge, since there were so many individual operations that needed to happen, either connected or otherwise (e.g. file parsing, address handling, disk read/write). However, getting the boiler plate ready much earlier on did lead to a much easier development process, since we could just grab what we needed without it being ridiculously inefficient. The workbench was already set up. 

Another main challenge was traversing the tree in-order. We thought initially that having the traversal method be recursive was going to be a problem, but this was before we had understood the purpose of DiskReadWrite. This lead us to believe we needed to write an iterative version, which caused <b>several</b> headaches. Eventually we just asked for help from tutors who walked me through the end goal of what needed to happen, and I was able to get it working. Massive thanks to Aaron for all the help on this project. 
## Reflection (Team member name: Joshua Thomas)
There was a lot that I deeply enjoyed about this project. It felt like a much more accurate reflection of how
programming work gets completed in the "real world". It was nice to get much more hands-on practice with scrum and the
core concepts of agile development. There was a really positive sense of accomplishment every time that I submitted a
commit with a "Closes #x" message.

Our biggest challenge was definitely in getting the BTree to work correctly. In particular, we had issues with getting
disk operations working as they should. This required a lot of digging into the documentation for the various classes
we were using during our disk procedures, as well as the pseudocode and definitions for implementation of B-Tree
methods.

We spent a large amount of time up front architecting the overall shape of the project so that we knew how things would
fit together. This allowed us to split our time and work on many things simultaneously if we hit roadblocks in a certain
area. This also led to an immensely satisfying moment at the end, where we finally got disk read/write operations 
functional, and as soon as we did, the program passed basically every test it was supposed to.

# Additional Notes
BTree data file is generated in the same directory as the gbk files.

To run this program:
Build it first with:
`./gradlew createJarGeneBankCreateBTree && ./gradle createJarGeneBankSearchBTree`

Then create the data file with:
`java -jar build/libs/GeneBankCreateBTree.jar --cache=<0|1>  --degree=<btree degree> 
	--gbkfile=<gbk file> --length=<sequence length> [--cachesize=<n>] [--debug=0|1|2]`

To search through the data file, run: 
`java -jar build/libs/GeneBankSearchBTree.jar --cache=<0/1> --degree=<btree degree> --btreefile=<BTree file> --length=<sequence length> --queryfile=<query file> [--cachesize=<n>] [--debug=0|1|2]`


