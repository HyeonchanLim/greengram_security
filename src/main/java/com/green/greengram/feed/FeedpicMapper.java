package com.green.greengram.feed;

import com.green.greengram.feed.model.FeedPicDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedpicMapper {
    int insFeedpic (FeedPicDto p);
    int insFeedpic2(FeedPicDto p);
    List<String> selFeedPicList(long feedId);
}
