package edu.ualberta.cs.papersdb.model.publication;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import edu.ualberta.cs.papersdb.model.Publisher;

@Entity
@DiscriminatorValue("Workshop")
public class WorkshopPub extends Publication {
    private static final long serialVersionUID = 1L;

    private String bookTitle;
    private Publisher bookPublisher;
    private String editor;
    private String edition;
    private String volume;
    private String number;
    private String pages;

    @Column(name = "BOOK_TITLE")
    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    @Column(name = "BOOK_PUBLISHER")
    public Publisher getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPpublisher(Publisher bookPpublisher) {
        this.bookPublisher = bookPpublisher;
    }

    @Column(name = "EDITOR")
    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    @Column(name = "EDITION")
    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    @Column(name = "VOLUME")
    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Column(name = "NUMBER")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "PAGES")
    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

}
