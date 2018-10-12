import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ExternalSortTest {
	private void test(String f1, String f2, String checksum){
		try {
			ExternalSort.sort(f1,f2);
			assertEquals(ExternalSort.checkSum(f1), checksum);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			assert(false);
		}
	}

	@Test
	public void test01(){
		test("test-suite/test1a.dat", "test-suite/test1b.dat", "d41d8cd98f0b24e980998ecf8427e");
	}
	@Test
	public void test02(){
		test("test-suite/test2a.dat", "test-suite/test2b.dat", "a54f041a9e15b5f25c463f1db7449");
	}
	@Test
	public void test03(){
		test("test-suite/test3a.dat", "test-suite/test3b.dat", "c2cb56f4c5bf656faca0986e7eba38");
	}
	@Test
	public void test04(){
		test("test-suite/test4a.dat", "test-suite/test4b.dat", "c1fa1f22fa36d331be4027e683baad6");
	}
	@Test
	public void test05(){
		test("test-suite/test5a.dat", "test-suite/test5b.dat", "8d79cbc9a4ecdde112fc91ba625b13c2");
	}
	@Test
	public void test06(){
		test("test-suite/test6a.dat", "test-suite/test6b.dat", "1e52ef3b2acef1f831f728dc2d16174d");
	}
	@Test
	public void test07(){
		test("test-suite/test7a.dat", "test-suite/test7b.dat", "6b15b255d36ae9c85ccd3475ec11c3");
	}
	@Test
	public void test08(){
		test("test-suite/test8a.dat", "test-suite/test8b.dat", "1484c15a27e48931297fb6682ff625");
	}
	@Test
	public void test09(){
		test("test-suite/test9a.dat", "test-suite/test9b.dat", "ad4f60f065174cf4f8b15cbb1b17a1bd");
	}
	@Test
	public void test10(){
		test("test-suite/test10a.dat", "test-suite/test10b.dat", "32446e5dd58ed5a5d7df2522f0240");
	}
	@Test
	public void test11(){
		test("test-suite/test11a.dat", "test-suite/test11b.dat", "435fe88036417d686ad8772c86622ab");
	}	
	@Test
	public void test12(){
		test("test-suite/test12a.dat", "test-suite/test12b.dat", "c4dacdbc3c2e8ddbb94aac3115e25aa2");
	}	
	@Test
	public void test13(){
		test("test-suite/test13a.dat", "test-suite/test13b.dat", "3d5293e89244d513abdf94be643c630");
	}	
	@Test
	public void test14(){
		test("test-suite/test14a.dat", "test-suite/test14b.dat", "468c1c2b4c1b74ddd44ce2ce775fb35c");
	}	
	@Test
	public void test15(){
		test("test-suite/test15a.dat", "test-suite/test15b.dat", "79d830e4c0efa93801b5d89437f9f3e");
	}	
	@Test
	public void test16(){
		test("test-suite/test16a.dat", "test-suite/test16b.dat", "c7477d400c36fca5414e0674863ba91");
	}	
	@Test
	public void test17(){
		test("test-suite/test17a.dat", "test-suite/test17b.dat", "cc80f01b7d2d26042f3286bdeff0d9");
	}
}
