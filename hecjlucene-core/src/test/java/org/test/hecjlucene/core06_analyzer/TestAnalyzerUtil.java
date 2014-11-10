package org.test.hecjlucene.core06_analyzer;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
public class TestAnalyzerUtil {

	@Test
	public void test01() {
		Analyzer analyzer1 = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer analyzer2 = new StopAnalyzer(Version.LUCENE_35);
		Analyzer analyzer3 = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer analyzer4 = new WhitespaceAnalyzer(Version.LUCENE_35);

		String txt = "this is my house,I am come from yunnang zhaotong,"
				+ "My email is ykfds@fd.com,"
				+ "My QQ IS 3453434";
		
		AnalyzerUtil.displayToken(txt, analyzer1);
		AnalyzerUtil.displayToken(txt, analyzer2);
		AnalyzerUtil.displayToken(txt, analyzer3);
		AnalyzerUtil.displayToken(txt, analyzer4);
	}
	/**
	 * @函数功能说明 经测试不支持中文文词
	 * @修改作者名字 JECJ  
	 * @修改时间 2014年11月3日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void test02() {
		Analyzer analyzer1 = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer analyzer2 = new StopAnalyzer(Version.LUCENE_35);
		Analyzer analyzer3 = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer analyzer4 = new WhitespaceAnalyzer(Version.LUCENE_35);
		
		String txt = "我来自安徽省界首市！";
		
		AnalyzerUtil.displayToken(txt, analyzer1);
		AnalyzerUtil.displayToken(txt, analyzer2);
		AnalyzerUtil.displayToken(txt, analyzer3);
		AnalyzerUtil.displayToken(txt, analyzer4);
	}
	@Test
	public void test03() {
		Analyzer analyzer1 = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer analyzer2 = new StopAnalyzer(Version.LUCENE_35);
		Analyzer analyzer3 = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer analyzer4 = new WhitespaceAnalyzer(Version.LUCENE_35);
		
		String txt = "how are you thank you";
		System.out.println(txt);
		AnalyzerUtil.displayAllTokenInfo(txt, analyzer1);
		System.out.println("----------------------------");
		AnalyzerUtil.displayAllTokenInfo(txt, analyzer2);
		System.out.println("----------------------------");
		AnalyzerUtil.displayAllTokenInfo(txt, analyzer3);
		System.out.println("----------------------------");
		AnalyzerUtil.displayAllTokenInfo(txt, analyzer4);
	}
	
	@Test
	public void test04() {
		/*自定义分词器*/
		Analyzer analyzer = new MyStopAnalyzer(new String[]{"you"});
		Analyzer analyzer1 = new StandardAnalyzer(Version.LUCENE_35);
		String txt = "how are you thank you";
		System.out.println(txt);
		AnalyzerUtil.displayAllTokenInfo(txt, analyzer);
		System.out.println("----------------------------------");
		AnalyzerUtil.displayAllTokenInfo(txt, analyzer1);
	}
	@Test
	public void test05() {
		Analyzer analyzer = new MMSegAnalyzer(new File("src/main/resources/mmseg4j/data"));
		String str = "我来自安徽合肥一个学校。白云山脚下昭通张杰，何超杰作";
		AnalyzerUtil.displayToken(str, analyzer);
	}
	/**
	 * @函数功能说明 同义词
	 * @修改作者名字 HECJ  
	 * @修改时间 2014年11月10日
	 * @修改内容
	 * @参数：     
	 * @return void   
	 * @throws
	 */
	@Test
	public void test06() {
		Analyzer analyzer = new MySameAnalyzer();
		String str = "我来自中国的一个学校。白云山脚下昭通张杰，何超杰作";
		AnalyzerUtil.displayToken(str, analyzer);
		System.out.println("------------------------------");
		AnalyzerUtil.displayAllTokenInfo(str, analyzer);
		
	}

}
