package org.nfa.lucia.service;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LuceneQueryService {

	private static final Logger log = LoggerFactory.getLogger(LuceneQueryService.class);

	@Autowired
	private LuceneConfigService luceneConfigService;

	public TopDocs searchAll() {
		try {

			IndexReader indexReader = luceneConfigService.indexReader();
			try {

				// 创建Indexsearcher对象，需要指定IndexReader对象
				IndexSearcher indexSearcher = new IndexSearcher(indexReader);

				// 创建查询条件
				// 使用MatchAllDocsQuery查询索引目录中的所有文档
				Query query = new MatchAllDocsQuery();
				// 执行查询
				// 第一个参数是查询对象，第二个参数是查询结果返回的最大值
				TopDocs topDocs = indexSearcher.search(query, 10);

				// 查询结果的总条数
				log.info("查询结果的总条数：" + topDocs.totalHits);
				// 遍历查询结果
				// topDocs.scoreDocs存储了document对象的id
				// ScoreDoc[] scoreDocs = topDocs.scoreDocs;
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					// scoreDoc.doc属性就是document对象的id
					// int doc = scoreDoc.doc;
					// 根据document的id找到document对象
					Document document = indexSearcher.doc(scoreDoc.doc);
					// 文件名称
					log.info("fileName:{} fileSize:{} filePath:{}", document.get("fileName"), document.get("fileSize"), document.get("filePath"));
					// // 文件内容
					// log.info(document.get("fileContent"));
				}
				return topDocs;
			} finally {
				indexReader.close();
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
