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

package com.logiclodge.unitywebsocketserver.services.friendslist;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.logiclodge.unitywebsocketserver.core.ClientEvent;
import com.logiclodge.unitywebsocketserver.unitysocket.UnitySocketService;
import com.logiclodge.unitywebsocketserver.user.UserService;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
@Service
public class FriendsListService {

	private static final Logger LOGGER = LoggerFactory.getLogger("FriendsListService");

	@Autowired
	private UnitySocketService unitySocketService;

	@Autowired
	private UserService userService;

	@EventListener
	public void onFriendsListRequest(ClientEvent<FriendsListRequest> event) {
		LOGGER.warn("Got Friends List Request");

		WebSocketSession session = event.getSession();
		try {
			unitySocketService.sendMessage(getFriendsListResponse(session), session);
			LOGGER.warn("Sending extra dummy response.  Client should be able to deal with it.");
			unitySocketService.sendMessage(new KemonomimiResponse(), session);
		} catch (IOException ex) {
			LOGGER.error("Something Bad Happened", ex);
		}
	}

	@EventListener
	public void onAddFriendRequest(ClientEvent<AddFriendRequest> event) {
		LOGGER.warn("Got Add Friend Request");
		try {
			event.getSession().sendMessage(new TextMessage("Got Add Friend Request"));
		} catch (IOException ex) {
			LOGGER.error("Something Bad Happened", ex);
		}
	}

	private FriendsListResponse getFriendsListResponse(WebSocketSession session) {
		return new FriendsListResponse(userService.getFriendsList(userService.getUserFromSession(session)));
	}
}
