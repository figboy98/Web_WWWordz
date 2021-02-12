package wwwordz.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Trie implements Iterable<String> {

	Node root;

	public Trie() {
		root = new Node();
	}

	// Getters and Setters
	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public void put(String word) {
		if (root == null)
			root = new Node();
		// Invoke put function of class Node to insert the Word in the trie
		root.put(word, 0);
	}

	public String getRandomLargeWord() {
		StringBuffer string = new StringBuffer();
		// Invoke getRandoLargeWord of class Node to get a random large word from the
		// trie
		root.getRandomLargeWord(string);
		return string.toString();
	}

	@Override
	public Iterator<String> iterator() {
		return new NodeIterator();
	}

	public Search startSearch() {
		return new Search(root);
	}

	private class Node extends HashMap<Character, Node> {

		private static final long serialVersionUID = 1L;
		private HashMap<Character, Node> children = new HashMap<Character, Node>();
		private boolean isWord;
		private Character content;

		Node() {
			children = new HashMap<Character, Node>();
			isWord = false;
		}

		@SuppressWarnings("unused")
		Node(HashMap<Character, Node> a) {
			children = a;
			isWord = false;
		}

		// Getters and Setters
		public void setChildren(HashMap<Character, Node> children) {
			this.children = children;
		}

		public void setWord(boolean isWord) {
			this.isWord = isWord;
		}

		void setIsWord(Boolean a) {
			isWord = a;
		}

		void setContent(Character a) {
			content = a;
		}

		HashMap<Character, Node> getChildren() {
			return children;
		}

		boolean getIsWord() {
			return isWord;
		}

		Character getContent() {
			return content;
		}

		// Recursive function to insert words in the trie
		private void put(String word, int n) {
			if (n == word.length()) {
				setIsWord(true);
			} else {

				Character aux = word.charAt(n);
				Node temp;

				if (children.containsKey(aux) == true)
					temp = children.get(aux);

				else {
					temp = new Node();
					setContent(aux);
					children.put(aux, temp);
				}
				temp.put(word, n + 1);
			}

		}

		// Function to transform a keySet of characters in a ArrayList of characters
		ArrayList<Character> keySetToArrayList(Set<Character> a) {
			ArrayList<Character> list = new ArrayList<Character>();

			for (Character in : a) {
				list.add(in);
			}
			return list;

		}

		void getRandomLargeWord(StringBuffer string) {

			ArrayList<Character> list = keySetToArrayList(children.keySet());

			int size = list.size();
			if (size == 0)
				return;

			Random random = new Random();
			int index = (random.nextInt()) % size;
			if (index < 0)
				index = 0;

			string.append(list.get(index));

			children.get(list.get(index)).getRandomLargeWord(string);
		}

	}

	static class Search {

		Node node;

		Search(Node a) {
			node = a;
		}

		Search(Search search) {
			node = search.node;
		}

		// Getters and Setters

		public void setNode(Node node) {
			this.node = node;
		}

		Node getNode() {
			return node;
		}

		public boolean continueWith(char letter) {
			if (node.children.get(letter) == null)
				return false;
			node = node.children.get(letter);
			return true;

		}

		public boolean isWord() {
			return node.isWord;

		}

	}

	class NodeIterator extends Node implements Iterator<String>, Runnable {

		private static final long serialVersionUID = 1L;
		private String nextWord;
		private boolean terminated;
		private Thread thread;

		NodeIterator() {
			terminated = false;
			nextWord = null;
			thread = new Thread(this, "Node Iterator");
			thread.start();
		}

		public boolean isTerminated() {
			return terminated;
		}

		public void setTerminated(boolean terminated) {
			this.terminated = terminated;
		}

		public String getNextWord() {
			return nextWord;
		}

		void setNextWord(String word) {
			nextWord = word;
		}

		void setTerminated(Boolean a) {
			terminated = a;
		}

		public void run() {
			StringBuilder string = new StringBuilder();
			searchWords(root, string);

			synchronized (this) {
				terminated = true;
				handshake();
			}
		}

		@Override
		public boolean hasNext() {
			synchronized (this) {
				if (!terminated) {
					handshake();
				}
			}
			return nextWord != null;
		}

		@Override
		public String next() {
			String word = nextWord;
			synchronized (this) {
				nextWord = null;
			}
			return word;

		}

		// Function to stop a thread and resume the work of the other
		private void handshake() {
			notify();
			try {
				wait();
			} catch (InterruptedException cause) {
				throw new RuntimeException("Unexpected interruption while waiting", cause);
			}
		}

		// Recursive function to traverse the trie and collect words
		private void searchWords(Node curr, StringBuilder string) {

			if (curr.children == null)
				return;
			if (curr.isWord == true) {
				setNextWord(string.toString());
				synchronized (this) {
					handshake();
				}

			}
			ArrayList<Character> childList = keySetToArrayList(curr.children.keySet());
			for (int i = 0; i < childList.size(); i++) {
				if (curr.children != null) {
					char aux = childList.get(i);
					StringBuilder stringCopy = new StringBuilder(string);
					stringCopy.append(aux);
					searchWords(curr.children.get(aux), stringCopy);
				}
			}

		}

	}
}
