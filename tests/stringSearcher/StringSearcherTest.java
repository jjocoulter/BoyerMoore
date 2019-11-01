package stringSearcher;

import arrayGenerator.CharacterArrayGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tester for string searchers.
 *
 * @author Hugh Osborne
 * @version October 2019
 */
abstract class StringSearcherTest {

    abstract StringSearcher getSearcher(String string);
    private CharacterArrayGenerator generator = new CharacterArrayGenerator();

    private int test(String substring,String superstring) throws NotFound {
        return getSearcher(substring).occursIn(superstring);
    }

    @Test
    void testFred() throws NotFound {
        assertEquals(2,test("fred","Alfred the Great"));
    }

    @Test
    void testAbbaabba() throws NotFound{
        assertEquals( 12, test("abbaabba", "bbbaababaaababbaabbabbaa"));
    }

    @Test
    void testCap() throws NotFound {
        assertEquals(6,test("cap","The incapable captain capsized the boat"));
    }

    @Test
    void testCab() {
        assertThrows(NotFound.class,()->test("absent","The can sent a message to base"));
    }

    @Test
    void testNotFound(){
        assertThrows(NotFound.class, ()->test(generator.getArray(6).toString(),
                generator.getArray(1000).toString()));
    }
}