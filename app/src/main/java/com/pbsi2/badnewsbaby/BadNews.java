package com.pbsi2.badnewsbaby;/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * BadNews represents an news item. It holds the details
 * of that event such as title, as well as time.
 */
public class BadNews {

    /**
     * Title of the article
     */
    public final String title;

    /**
     * Section of the article
     */
    public final String section;

    /**
     * Author of the article
     */
    public final String author;
    /**
     * Author of the article
     */
    public final String link;
    /**
     * Author of the article
     */
    public final String summary;
    /**
     * Tdate
     */
    public final String date;


    /**
     * Constructs a new {@link BadNews}.
     *
     * @param title   is the title o
     * @param section is the section
     * @param author  is whether or not a author is listed
     * @param link    is the link tp the source
     * @param summary is the description
     * @param date    is ,well, the date
     */
    public BadNews(String title, String section, String author, String link, String summary, String date) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.link = link;
        this.summary = summary;
        this.date = date;

    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getauthor() {
        return author;
    }

    public String getlink() {
        return link;
    }

    public String getsummary() {
        return summary;
    }

    public String getdate() {
        return date;
    }

}
