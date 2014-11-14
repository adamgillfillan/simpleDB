/*
 * This is the part we add to SimpleDB
 Editing
 */
package simpledb.buffer;

/**
 *
 * @author Nakul
 */
public class ClockReplacementPolicy {
    private int framePointer = 0;
    
    int forReplacement(Buffer[] pool){
        int unPinned= -1;
        
        Buffer currentBuffer;
        
        while (unPinned == -1){
            currentBuffer = pool[framePointer];
            
            if (!currentBuffer.isPinned())
            {
                if (currentBuffer.counter > 0){
                    currentBuffer.counter--;
                    
                }
                else {
                    unPinned = framePointer;
                }
            }
        }
    return unPinned;
    }
}
