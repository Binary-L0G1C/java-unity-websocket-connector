/*
 * Copyright 2017 L0G1C (David B) - logiclodge.com
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

package com.logiclodge.unitywebsocketserver.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Currently used to mock out objects that would normally be stored in a
 * database.
 *
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
@Service
public class UserDao {

	private final Map<String, User> users = new CaseInsensitiveMap<>();
	private final FriendsListLinks friendsListLinks = new FriendsListLinks();

	@Autowired
	public UserDao() {
		initializeTestData();
	}

	public User getUser(String username) {
		return users.get(username);
	}

	public Collection<User> getUsers() {
		return Collections.unmodifiableCollection(users.values());
	}

	public void addUser(User user) {
		if (users.containsKey(user.getUsername())) {
			throw new IllegalArgumentException("Tried to add a duplicate user");
		}
		users.put(user.getUsername(), user);
	}

	public Collection<User> getFriendsList(User user) {
		return Collections.unmodifiableCollection(friendsListLinks.getFriends(user));
	}

	public void addFriendship(User user1, User user2) {
		friendsListLinks.addLink(user1, user2);
	}

	public void removeFriendship(User user1, User user2) {
		friendsListLinks.deleteLink(user1, user2);
	}

	private class FriendsListLinks {
		private Map<User, Collection<User>> usersFriends = new HashMap<>();

		public void addLink(User user1, User user2) {
			getUsers(user1).add(user2);
			getUsers(user2).add(user1);
		}

		public void deleteLink(User user1, User user2) {
			getUsers(user1).remove(user2);
			getUsers(user2).remove(user1);
		}

		public Collection<User> getFriends(User user) {
			return getUsers(user);
		}

		private Collection<User> getUsers(User user) {
			if (!usersFriends.containsKey(user)) {
				usersFriends.put(user, new HashSet<>());
			}
			return usersFriends.get(user);
		}
	}

	private void initializeTestData() {
		User user1 = new User("CatgirlLover", "nekomimi");
		User user2 = new User("David", "nekomimi");
		User user3 = new User("Fred", "megane");
		//
		User u1 = new User("TestUser1", "a"); // Just the letter a... alright...
		User u2 = new User("TestUser2", "a");
		User u3 = new User("TestUser3", "a");
		User u4 = new User("TestUser4", "a");
		User u5 = new User("TestUser5", "a");
		User u6 = new User("TestUser6", "a");
		User u7 = new User("TestUser7", "a");
		User u8 = new User("TestUser8", "a");
		User u9 = new User("TestUser9", "a");

		addUser(user1);
		addUser(user2);
		addUser(user3);
		//
		addUser(u1);
		addUser(u2);
		addUser(u3);
		addUser(u4);
		addUser(u5);
		addUser(u6);
		addUser(u7);
		addUser(u8);
		addUser(u9);

		addFriendship(user1, user2);
		addFriendship(user3, user1);
		//
		addFriendship(user2, u1);
		addFriendship(user2, u2);
		addFriendship(user2, u3);
		addFriendship(user2, u5);
		addFriendship(user2, u7);
		addFriendship(user2, u8);
		addFriendship(user2, u9);
		//
		addFriendship(user3, u1);
		addFriendship(user3, u2);
		addFriendship(user3, u4);
		addFriendship(user3, u6);
		addFriendship(user3, u8);
		addFriendship(user3, u9);

	}
}
