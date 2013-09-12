/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flow;

import com.example.flow.model.User;
import com.example.flow.view.ConversationListView;
import com.example.flow.view.ConversationView;
import com.example.flow.view.FriendListView;
import com.example.flow.view.FriendView;
import com.example.flow.view.MessageView;
import com.squareup.flow.HasParent;
import com.squareup.flow.Screen;
import com.squareup.flow.annotation.Layout;
import com.squareup.flow.annotation.View;
import dagger.Module;
import dagger.Provides;

public @interface App {
  @View(ConversationListView.class) //
  @Module(injects = ConversationListView.class, addsTo = MainActivity.ActivityModule.class)
  public static class ConversationList implements Screen {
  }

  @View(ConversationView.class) //
  @Module(injects = ConversationView.class, addsTo = MainActivity.ActivityModule.class)
  public static class Conversation implements Screen, HasParent<ConversationList> {
    public final int conversationIndex;

    public Conversation(int conversationIndex) {
      this.conversationIndex = conversationIndex;
    }

    @Provides com.example.flow.model.Conversation provideConversation() {
      return SampleData.CONVERSATIONS.get(conversationIndex);
    }

    @Override public ConversationList getParent() {
      return new ConversationList();
    }
  }

  @Layout(R.layout.message_view) //
  @Module(injects = MessageView.class, addsTo = MainActivity.ActivityModule.class)
  public static class Message implements Screen, HasParent<Conversation> {
    public final int conversationIndex;
    public final int messageId;

    public Message(int conversationIndex, int messageId) {
      this.conversationIndex = conversationIndex;
      this.messageId = messageId;
    }

    @Provides com.example.flow.model.Conversation.Item provideMessage() {
      return SampleData.CONVERSATIONS.get(conversationIndex).items.get(messageId);
    }

    @Override public Conversation getParent() {
      return new Conversation(conversationIndex);
    }
  }

  @View(FriendListView.class) //
  @Module(injects = FriendListView.class, addsTo = MainActivity.ActivityModule.class)
  public static class FriendList implements Screen, HasParent<ConversationList> {
    @Override public ConversationList getParent() {
      return new ConversationList();
    }
  }

  @View(FriendView.class) //
  @Module(injects = FriendView.class)
  public static class Friend implements Screen, HasParent<FriendList> {
    public final int index;

    public Friend(int index) {
      this.index = index;
    }

    @Provides User provideFriend() {
      return SampleData.FRIENDS.get(index);
    }

    @Override public FriendList getParent() {
      return new FriendList();
    }
  }
}
