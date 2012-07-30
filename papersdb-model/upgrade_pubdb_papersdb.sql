-- pubDB to papersdb migrate script

-- fix errors
UPDATE user SET name='Pandora Lam' WHERE name='PandoraLam';
UPDATE user SET name='Idanis Diaz' WHERE name='idanis';
DELETE FROM user WHERE name='nelson';
UPDATE publication SET updated=null WHERE updated='0000-00-00';
UPDATE author SET email=null WHERE trim(email)='';
UPDATE author SET email=null WHERE `name`='Lake, Robert';

DELETE ua FROM user_author ua LEFT JOIN `user` ON user.login=ua.login WHERE user.login is null;

DELETE pa FROM pub_author pa LEFT JOIN publication pub ON pub.pub_id=pa.pub_id WHERE pub.pub_id is null;

UPDATE publication pub,venue,(SELECT `name`,MIN(venue_id) venue_id,COUNT(*) cnt FROM venue GROUP BY `name`) a
       SET pub.venue_id=a.venue_id
       WHERE venue.venue_id=pub.venue_id
       AND a.name=venue.name
       AND a.cnt > 1;

DELETE venue FROM venue LEFT JOIN publication pub ON pub.venue_id=venue.venue_id WHERE pub.pub_id is null;

DELETE FROM user WHERE verified=0;

--

ALTER TABLE `user` DROP PRIMARY KEY,
      ADD COLUMN `ID` bigint(20) NOT NULL auto_increment FIRST,
      ADD PRIMARY KEY (`ID`) USING BTREE;

ALTER TABLE `user`
      ADD COLUMN `VERSION` int(11) NOT NULL AFTER `ID`,
      CHANGE `access_level` `ACCESS_TYPE` int(11) NULL AFTER `VERSION`,
      CHANGE `email` `EMAIL` varchar(255) NULL AFTER `ACCESS_TYPE`,
      CHANGE `name` `FAMILY_NAMES` varchar(255) NULL AFTER `EMAIL`,
      ADD COLUMN `GIVEN_NAMES` varchar(255) NULL AFTER `FAMILY_NAMES`,
      ADD COLUMN `LAST_LOGIN` datetime NULL AFTER `GIVEN_NAMES`,
      CHANGE `login` `LOGIN` varchar(255) NULL AFTER `LAST_LOGIN`,
      CHANGE `password` `PASSWORD` varchar(255) NULL AFTER `LOGIN`,
      ADD COLUMN `REGISTRATION_DATE` datetime NULL AFTER `PASSWORD`,
      CHANGE `verified` `IS_VERIFIED` tinyint(1) NULL AFTER `REGISTRATION_DATE`,
      DROP COLUMN `search`,
      DROP COLUMN `comments`,
      DROP COLUMN `options`,
      ADD UNIQUE INDEX `EMAIL` (`EMAIL`) USING BTREE,
      ADD UNIQUE INDEX `LOGIN` (`LOGIN`) USING BTREE,
      ENGINE=InnoDB ROW_FORMAT=Compact;

ALTER TABLE user MODIFY COLUMN ID BIGINT(20) NOT NULL;

-- breakup name into first and last names

UPDATE user SET given_names=TRIM(LEFT(family_names,LOCATE(' ',family_names)));
UPDATE user SET family_names=TRIM(SUBSTR(family_names,LOCATE(' ',family_names)));

--

ALTER TABLE author ENGINE=InnoDB;

ALTER TABLE `author` DROP PRIMARY KEY,
      CHANGE COLUMN `author_id` `ID` bigint(20) NOT NULL  FIRST,
      ADD PRIMARY KEY (`ID`) USING BTREE;

ALTER TABLE author
      ADD COLUMN VERSION INT(11) NOT NULL COMMENT '' AFTER `ID`,
      CHANGE COLUMN `email` EMAIL VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '',
      CHANGE COLUMN `name` FAMILY_NAMES VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '',
      ADD COLUMN GIVEN_NAMES VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '' AFTER family_names,
      CHANGE COLUMN `title` TITLE VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '',
      ADD CONSTRAINT EMAIL UNIQUE KEY(EMAIL);

ALTER TABLE author
      DROP COLUMN webpage,
      DROP COLUMN organization;


UPDATE author SET given_names=TRIM(SUBSTR(family_names,LOCATE(',',family_names)+1));
UPDATE author SET family_names=TRIM(LEFT(family_names,LOCATE(',',family_names)-1));

--

ALTER TABLE user_author ENGINE=InnoDB;

ALTER TABLE user_author
      ADD COLUMN USER_ID BIGINT(20) COMMENT '' NOT NULL,
      MODIFY COLUMN AUTHOR_ID BIGINT(20) COMMENT '' NOT NULL;
UPDATE user_author,`user` SET user_id=user.id WHERE user.login=user_author.login;

ALTER TABLE user_author
      DROP PRIMARY KEY,
      DROP COLUMN login,
      ADD INDEX FKF581647FA32D0B24 (AUTHOR_ID),
      ADD INDEX FKF581647FAF21CD3 (USER_ID),
      ADD PRIMARY KEY (USER_ID, AUTHOR_ID);

ALTER TABLE user_author
      ADD CONSTRAINT FKF581647FA32D0B24 FOREIGN KEY FKF581647FA32D0B24 (AUTHOR_ID)
          REFERENCES author (ID) ON UPDATE NO ACTION ON DELETE NO ACTION,
      ADD CONSTRAINT FKF581647FAF21CD3 FOREIGN KEY FKF581647FAF21CD3 (USER_ID)
          REFERENCES user (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

--

UPDATE publication SET rank_id=6 WHERE rank_id=5;
UPDATE publication SET rank_id=5 WHERE rank_id=0;

RENAME TABLE publication TO paper;
ALTER TABLE paper ENGINE=InnoDB;

ALTER TABLE paper
      DROP PRIMARY KEY,
      CHANGE COLUMN `pub_id` `ID` bigint(20) NOT NULL  FIRST,
      ADD PRIMARY KEY (`ID`) USING BTREE;

ALTER TABLE paper
      CHANGE `title` TITLE VARCHAR(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
      CHANGE `abstract` ABSTRACT TEXT
             CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
      CHANGE `keywords` KEYWORDS VARCHAR(255)
             CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
      CHANGE `extra_info` EXTRA_INFORMATION TEXT
             CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
      CHANGE `published` PAPER_DATE DATE NULL DEFAULT NULL,
      CHANGE `rank_id` RANKING_ID INT(11) NULL DEFAULT NULL,
      CHANGE `updated` DB_UPDATE_DATE DATETIME NULL DEFAULT NULL,
      CHANGE `user`  USER_TAGS VARCHAR(255)
             CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
      ADD COLUMN VERSION INT(11) NOT NULL AFTER `ID`,
      ADD COLUMN DOI VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
      ADD COLUMN CUSTOM_RANKING VARCHAR(255)
             CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
      ADD COLUMN DB_INSERT_DATE DATETIME NULL DEFAULT NULL,
      ADD COLUMN PUBLIC TINYINT(1) NULL DEFAULT NULL,
      ADD COLUMN  PUBLICATION_ID BIGINT(20) NULL DEFAULT NULL,
      ADD COLUMN  SUBMITTED_BY_USER_ID BIGINT(20) NULL DEFAULT NULL,
      ADD INDEX FK486196CC5699AAF (SUBMITTED_BY_USER_ID),
      ADD INDEX FK_PAPER_PUBLICATION (PUBLICATION_ID),
      ADD CONSTRAINT TITLE UNIQUE KEY(TITLE, PUBLICATION_ID),
      ADD CONSTRAINT DOI UNIQUE KEY(DOI);

ALTER TABLE paper
      ADD CONSTRAINT FK486196CC5699AAF FOREIGN KEY (SUBMITTED_BY_USER_ID)
          REFERENCES user (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;
UPDATE paper,pub_rankings pr
       SET custom_ranking=pr.description
       WHERE pr.pub_id is not null AND paper.id=pr.pub_id;

DROP TABLE pub_rankings;

CREATE TABLE paper_attachment (
    PAPER_ID BIGINT(20) NOT NULL,
    ATTACHMENT VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
    INDEX FK8C91A5D6EB041BB0 (PAPER_ID)
) ENGINE=InnoDB COLLATE=latin1_swedish_ci;

ALTER TABLE paper_attachment
      ADD CONSTRAINT FK8C91A5D6EB041BB0 FOREIGN KEY (PAPER_ID)
      REFERENCES paper (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO paper_attachment (paper_id, attachment)
       SELECT id,paper FROM paper;

INSERT INTO paper_attachment (paper_id, attachment)
       SELECT pub_id,location FROM additional_info ai
       JOIN pub_add ON pub_add.add_id=ai.add_id;

ALTER TABLE paper DROP COLUMN paper;
DROP TABLE additional_info;
DROP TABLE attachment_types;
DROP TABLE pub_add;

CREATE TABLE paper_paper (
    FROM_PAPER_ID BIGINT(20) NOT NULL,
    TO_PAPER_ID BIGINT(20) NOT NULL,
    INDEX FK7B92D4D9F82834A5 (FROM_PAPER_ID),
    INDEX FK7B92D4D953E258F4 (TO_PAPER_ID),
    PRIMARY KEY (FROM_PAPER_ID, TO_PAPER_ID)
) ENGINE=InnoDB COLLATE=latin1_swedish_ci;

ALTER TABLE paper_paper
      ADD CONSTRAINT FK7B92D4D953E258F4 FOREIGN KEY (TO_PAPER_ID)
          REFERENCES paper (ID) ON UPDATE NO ACTION ON DELETE NO ACTION,
      ADD CONSTRAINT FK7B92D4D9F82834A5 FOREIGN KEY (FROM_PAPER_ID)
          REFERENCES paper (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO paper_paper (FROM_PAPER_ID, TO_PAPER_ID)
       SELECT pub_id,`value` FROM pointer
       JOIN paper from_paper ON from_paper.id=pointer.pub_id
       JOIN paper to_paper ON to_paper.id=pointer.value
       WHERE `type`='int';

CREATE TABLE paper_url (
    PAPER_ID BIGINT(20) NOT NULL,
    URL VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
    INDEX FKB7EFFBFCEB041BB0 (PAPER_ID)
) ENGINE=InnoDB COLLATE=latin1_swedish_ci;

ALTER TABLE paper_url
      ADD CONSTRAINT FKB7EFFBFCEB041BB0 FOREIGN KEY (PAPER_ID)
          REFERENCES paper (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO paper_url (PAPER_ID, URL)
       SELECT pub_id,`value` FROM pointer
       JOIN paper paper ON paper.id=pointer.pub_id
       WHERE `type`='ext';

DROP TABLE pointer;

CREATE TABLE paper_collaboration (
    PAPER_ID BIGINT(20) NOT NULL,
    COLLABORATION_ID VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
    INDEX FK9F371092EB041BB0 (PAPER_ID),
    PRIMARY KEY (PAPER_ID, COLLABORATION_ID)
) ENGINE=InnoDB COLLATE=latin1_swedish_ci;

ALTER TABLE paper_collaboration
      ADD CONSTRAINT FK9F371092EB041BB0 FOREIGN KEY (PAPER_ID)
          REFERENCES paper (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO paper_collaboration (PAPER_ID, COLLABORATION_ID)
       SELECT pub_col.pub_id,pub_col.col_id FROM pub_col
       JOIN collaboration cb ON cb.col_id=pub_col.col_id
       JOIN paper pb ON pb.id=pub_col.pub_id;


DROP TABLE collaboration;
DROP TABLE pub_col;

--

RENAME TABLE pub_author TO author_ranked;
ALTER TABLE author_ranked ENGINE=InnoDB;

ALTER TABLE `author_ranked` DROP PRIMARY KEY,
      ADD COLUMN `ID` bigint(20) NOT NULL auto_increment FIRST,
      ADD PRIMARY KEY (`ID`) USING BTREE;

ALTER TABLE `author_ranked`
    ADD COLUMN VERSION INT(11) NOT NULL AFTER `ID`,
    CHANGE COLUMN `rank` RANK INT(11) NULL DEFAULT NULL,
    CHANGE COLUMN `author_id` AUTHOR_ID BIGINT(20) NOT NULL,
    CHANGE COLUMN `pub_id` PAPER_ID BIGINT(20) NULL NOT NULL,
    ADD INDEX FK_AUTHOR_RANKED_AUTHOR (AUTHOR_ID),
    ADD INDEX FK_AUTHOR_RANKED_PAPER (PAPER_ID);

ALTER TABLE author_ranked
      ADD CONSTRAINT FK_AUTHOR_RANKED_PAPER FOREIGN KEY (PAPER_ID)
          REFERENCES paper (ID) ON UPDATE NO ACTION ON DELETE NO ACTION,
      ADD CONSTRAINT FK_AUTHOR_RANKED_AUTHOR FOREIGN KEY (AUTHOR_ID)
          REFERENCES author (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE user MODIFY COLUMN ID BIGINT(20) NOT NULL;

--

UPDATE venue SET `name`=title WHERE name is null or trim(name)='';

UPDATE venue SET rank_id=6 WHERE rank_id=5;
UPDATE venue SET rank_id=5 WHERE rank_id=0;

RENAME TABLE venue TO publisher;
ALTER TABLE publisher ENGINE=InnoDB;

ALTER TABLE publisher
      CHANGE COLUMN `venue_id` `ID` bigint(20) NOT NULL FIRST,
      ADD COLUMN VERSION INT(11) NOT NULL AFTER `ID`,
      CHANGE `name` `NAME` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
      CHANGE `title` `ACRONYM` VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
      CHANGE `url` URL VARCHAR(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
      CHANGE `rank_id` RANKING_ID int(11) DEFAULT NULL,
      ADD COLUMN `CUSTOM_RANKING` varchar(255) DEFAULT NULL AFTER RANKING_ID,
      DROP COLUMN editor,
      DROP COLUMN `date`,
      ADD CONSTRAINT NAME UNIQUE KEY(NAME);

--

\q

ALTER TABLE paper
      ADD CONSTRAINT FK_PAPER_PUBLICATION FOREIGN KEY (PUBLICATION_ID)
          REFERENCES publication (ID) ON UPDATE NO ACTION ON DELETE NO ACTION;

DROP TABLE aicml_positions;
DROP TABLE aicml_staff;
DROP TABLE author_interest;
DROP TABLE cat_info;
DROP TABLE cat_vopts;
DROP TABLE category;
DROP TABLE extra_info;
DROP TABLE help_fields;
DROP TABLE info;
DROP TABLE interest;
DROP TABLE pub_cat;
DROP TABLE pub_cat_info;
DROP TABLE pub_pending;
DROP TABLE pub_valid;
DROP TABLE tag_ml_history;
DROP TABLE venue_occur;
DROP TABLE venue_rankings;
DROP TABLE venue_vopts;
DROP TABLE vopts;

CREATE TABLE `publication` (
  `DISCRIMINATOR` varchar(31) NOT NULL,
  `ID` bigint(20) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `DATE` datetime NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `BOOK_TITLE` varchar(255) DEFAULT NULL,
  `EDITION` varchar(255) DEFAULT NULL,
  `EDITOR` varchar(255) DEFAULT NULL,
  `CHAPTER` varchar(255) DEFAULT NULL,
  `NUMBER` varchar(255) DEFAULT NULL,
  `PAGES` varchar(255) DEFAULT NULL,
  `VOLUME` varchar(255) DEFAULT NULL,
  `INSTITUTION` varchar(255) DEFAULT NULL,
  `THESIS_TYPE` varchar(255) DEFAULT NULL,
  `BOOK_PUBLISHER` tinyblob,
  `PUBLISHER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PUBLICATION_PUBLISHER` (`PUBLISHER_ID`),
  CONSTRAINT `FK_PUBLICATION_PUBLISHER` FOREIGN KEY (`PUBLISHER_ID`) REFERENCES `publisher` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

