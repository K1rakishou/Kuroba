package com.github.adamantcheese.chan.core.di.module.application;

import com.android.volley.RequestQueue;
import com.github.adamantcheese.chan.core.site.parser.CommentParser;
import com.github.adamantcheese.chan.core.site.parser.CommentParserHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ParserModule {

    @Provides
    @Singleton
    public CommentParser provideCommentParser() {
        return new CommentParser();
    }

    @Provides
    @Singleton
    public CommentParserHelper provideCommentParserHelper(RequestQueue requestQueue) {
        return new CommentParserHelper(requestQueue);
    }

}
