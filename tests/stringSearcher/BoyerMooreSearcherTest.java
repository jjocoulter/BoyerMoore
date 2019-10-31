package stringSearcher;

/**
 * Tests sequential string searchers.
 *
 * @author Hugh Osborne
 * @version October 2019
 */
class BoyerMooreSearcherTest extends StringSearcherTest {

    @Override
    StringSearcher getSearcher(String string) {
        return new BoyerMooreSearcher(string);
    }
}