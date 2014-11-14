simpleDB Project
========
Background
A bufferpool contains a set of buffer and each buffer can hold a page. Data is stored on disk as blocks and after read into main memory are represented as pages. Reading and writing pages from main memory to disk is an important task of a database system.  Main memory is partitioned into collections of pages. Data is stored on disk as blocks and after being read into main memory are represented as pages. Each page can be hold in a buffer and the collection of buffers is called the buffer pool. The buffer manager is responsible for bringing blocks from disk to the buffer pool when they are needed and writing blocks back to the disk when they have been updated.  The buffer manager keeps a pin count and dirty flag for each buffer in the buffer pool.  The pin count records the number of times a page has been requested but not released, and the dirty flag records whether the page has been updated or not.  As the buffer pool fills, some pages may need to be removed in order to make room for new pages. The buffer manager uses a replacement policy to choose pages to be flushed from the buffer pool.  The strategy used can greatly affect the performance of the system. LRU (least recently used), MRU (most recently used) and Clock are different policies that are appropriate to use under different conditions.

SimpleDB  Buffer Manager (from Dr.Edward Sciore’s notes)
The SimpleDB buffer manager is grossly inefficient in two ways: 
•	When looking for a buffer to replace, it uses the first unpinned buffer it finds, instead of doing something intelligent replacement policy. 
•	When checking to see if a block is already in a buffer, it does a sequential scan of the buffers, instead of keeping a data structure (such as a map) to more quickly locate the buffer. 

Task Description
Below is a brief description of the tasks that you are required to implement and some additional guidelines that will help you with the implementation:
•	Task 1. The G-Clock Replacement Policy  
In the recorded lecture, we reviewed the Clock replacement policy. The G-Clock is an extension of the Clock policy. We will first review the Clock policy, highlighting some implementation details. Then give the description of the G-Clock policy. 
Clock Replacement Policy: The figure below illustrates the execution of the Clock algorithm. Conceptually, all the buffers in the buffer pool are arranged in a circle around the face of a Clock. Associated with each buffer is a referenced flag. Each time a page in the buffer pool is unpinned, the referenced flag of the corresponding buffer is set to true. Whenever we need to choose a buffer for replacement, the current frame pointer, or the "clock hand" (an integer whose value is between 0 and poolSize-1), is advanced, using modular arithmetic so that it does not go past poolSize-1. This corresponds to the clock hand moving around the face of the clock in a clockwise fashion. For each frame that the clock hand goes past that is unpinned, the referenced flag is examined and then cleared. If the flag had been set, the corresponding frame has been referenced "recently" and is not replaced. On the other hand, if the referenced flag is false, the page is selected for replacement. If the selected buffer frame is dirty (i.e. it has been modified), the page currently occupying the frame is written to disk. 
 
Clock Replacement Diagram 



Generalized Clock (G-Clock[i]) – In the generalized clock policy, a Reference Counter (RC, instead of reference bit) is maintained per page. Replacement is allowed for a page only if a page has reference counter value 0. Otherwise, the counter is decremented and the next page is inspected. There is a parameter i which is the threshold number from which we start counting down. So G-Clock(5) will initialize an unpinned page to 5 and then decrement from there. In effect, the regular Clock policy is actually, G-Clock(1). 

•	Task 2: Use a Map data structure to keep track of the buffer pool
Keep a Map of allocated buffers, keyed on the block they contain.  (A buffer is allocated when its contents is not null, and may be pinned or unpinned.  A buffer starts out unallocated; it becomes allocated when it is first assigned to a block, and stays allocated forever after.)  Use this map to determine if a block is currently in a buffer.  When a buffer is replaced, you must change the map -- The mapping for the old block must be removed, and the mapping for the new block must be added. 

•	Task 3: Revise SimpleDB so that it uses blocks as the elements of recovery.  A possible strategy is to save a copy of a block the first time a transaction modifies it.  The copy could be saved in a separate file, and the update log record could hold the block number of the copy.  You will also need to write methods that can copy blocks between files.




Comments:  

•	To avoid an infinite loop in the implementation, assume that the clock head spins at most i times to find a buffer for replacement. If an unpinned buffer cannot be found even after i clock rotations, the “pin” method in BasicBufferMgr should return null. This will not affect the timing policy of the “pin” in BufferMgr.
•	For our convenience, we will be using “bufferPoolMap” as the name of the Map. 
