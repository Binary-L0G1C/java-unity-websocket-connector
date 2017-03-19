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

using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using WebSocketSharp;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
public class FriendsListController : MonoBehaviour {

	public Text text;
	public String textString;

	private static readonly String responseName = "FRIENDSLIST";

	// Use this for initialization
	void Start() {
		NetworkingController networking = GameMaster.getNetworkingController();

		networking.startListenOnMessage(FriendsListUpdated, responseName);
		//networking.requestFriendsList();
		requestFriendsList(networking);
	}

	void OnDestroy() {
		NetworkingController networking = GameMaster.getNetworkingController();

		networking.stopListenOnMessage(FriendsListUpdated, responseName);
	}

	void Update() {
		text.text = textString;
	}

	private void requestFriendsList(NetworkingController networking) {
		var request = new FriendsListRequest();

		networking.sendRequest(request);
	}

	private void FriendsListUpdated(object sender, MessageEventArgs e) {
		Debug.Log("HEY COOL, Friends List Response!  Server says: " + e.Data);

		FriendsListResponse friendslist = FriendsListResponse.CreateFromJSON(e.Data);
		textString = "Friends:\n\n";
		foreach (string friend in friendslist.usernames) {
			textString += "- " + friend + "\n";
		}
	}

	public class FriendsListRequest : ClientRequest {
		public String friendsListRequestType;

		public FriendsListRequest() {
			type = "FRIENDSLIST";
			friendsListRequestType = "INITIAL";
		}
	}

	public class FriendsListResponse {
		public String type;
		public List<string> usernames;

		public static FriendsListResponse CreateFromJSON(string jsonString) {
			return JsonUtility.FromJson<FriendsListResponse>(jsonString);
		}
	}
}
