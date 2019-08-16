package com.alekseysamoylov.gridfs;

import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("file:src/main/resources/mongoConfig.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SaveFileToMongoTest {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GridFsTemplate gridFsTemplate;

  @Autowired
  private GridFsOperations operations;


  @Test
  public void findFile() throws IOException {

    GridFSFile file = operations.findOne(Query.query(Criteria.where("_id").is(new ObjectId("5b50309f24e499001290fbc3"))));
    GridFsResource resource = operations.getResource(file);
    logger.info("Resource length {}", resource.contentLength());

    try (InputStream inputStream = new FileInputStream("offer12259v2.pdf");
    ) {
      Document document = new Document();
      document.put("sourceSystem", "offer-generator");
      document.put("application-type", "RU_SOLVA");
      logger.info("File ID {}", operations.store(inputStream, file.getFilename(), "application/pdf", new Document()));
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

}
