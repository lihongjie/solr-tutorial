package example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class TestQuery {

    /**
     * 特定项搜索
     */
    @Test
    public void testTermQuery() {
        String indexDir = "E:\\lucenetemp\\demo3\\indexDir";

        String fld = "content";
        String text = "jre";
//        特定项片段名和关键字
        Term t = new Term(fld, text);
        TermQuery tq = new TermQuery(t);
        try {
            FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
//            注意:索引输入流不是new出来的，是通过目录读取工具类打开的
            IndexReader indexReader = DirectoryReader.open(indexDirectory);
//            获取索引搜索对象
            IndexSearcher is = new IndexSearcher(indexReader);


            TopDocs hits = is.search(tq, 100);
            System.out.println(hits.totalHits);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = is.doc(scoreDoc.doc);

                System.out.println("搜索结果: " + doc.get("content"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询表达式（queryParser）
     */
    @Test
    public void testQueryParser() {
        String indexDir = "E:\\lucenetemp\\demo3\\indexDir";
//        获取查询解析器（通过哪种分词器去解析哪种片段）
        QueryParser queryParser = new QueryParser("content", new StandardAnalyzer());
        try {
            FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
//            注意:索引输入流不是new出来的，是通过目录读取工具类打开的
            IndexReader indexReader = DirectoryReader.open(indexDirectory);
//            获取索引搜索对象
            IndexSearcher is = new IndexSearcher(indexReader);

//            由解析器去解析对应的关键字
            TopDocs hits = is.search(queryParser.parse("possible"), 100);
            System.out.println(hits.totalHits);
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = is.doc(scoreDoc.doc);
                System.out.println("搜索结果" + doc.get("content"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
