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

using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
public class LoginButton : MonoBehaviour {

	public InputField serverIp;
	public InputField username;
	public InputField password;
	public GameObject connectingPanel;
	public GameObject popupPanel;
	public Text errorText;

	public void login() {
		connectingPanel.SetActive(true);
		NetworkingController networking = GameMaster.getNetworkingController();
		networking.connect(serverIp.text, username.text, password.text);

		if (!networking.isConnected()) {
			//Text errorText = popupPanel.GetComponent<Text>();
			errorText.text = "Error Connecting to Server!";
			connectingPanel.SetActive(false);
			popupPanel.SetActive(true);
		} else {
			SceneManager.LoadScene("FriendsListScene", LoadSceneMode.Single);
		}
	}
}
