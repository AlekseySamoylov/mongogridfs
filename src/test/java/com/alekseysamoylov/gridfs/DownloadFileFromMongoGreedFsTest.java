package com.alekseysamoylov.gridfs;

import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
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
public class DownloadFileFromMongoGreedFsTest {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GridFsTemplate gridFsTemplate;

  @Autowired
  private GridFsOperations operations;


  @Test
  public void findFile() {

    List<GridFSFile> gridFSFiles = new ArrayList<>();
    gridFsTemplate
        .find(new Query(Criteria.where("metadata.externalId").is(1167138L)
            .and("metadata.sourceSystem").is("FINKARTA")))
        .into(gridFSFiles);
    logger.info(gridFSFiles.toString());

    gridFSFiles.forEach(file -> {
      GridFsResource resource = operations.getResource(file.getFilename());
      try (InputStream inputStream = resource.getInputStream()) {

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        String theString = writer.toString();
        logger.info(theString);
      } catch (IOException e) {
        logger.error(e.getMessage(), e);
      }
    });
  }
}
