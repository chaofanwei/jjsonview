package jsonview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonView extends JFrame implements MouseListener,ActionListener{

	private JTextArea area ;
	JTree jTree ;
	JPopupMenu popMenu;
	public JsonView(){
		setSize(800, 600);
	    setTitle("JJsonView");
	    layoutComponents();
	    
	    setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();  
	    setLocation((dim.width - getWidth()) / 2, (dim.height - getHeight()) / 2);
	}
	
	private void layoutComponents() {
		String[] tabNames = { "树", "文本" ,"url"}; 
		JTabbedPane jTabbedpane = new JTabbedPane();// 存放选项卡的组件 
		int i = 0;
		// 第一个标签下的JPanel
		JPanel jpanelFirst = new JPanel();
		jpanelFirst.setLayout(new BorderLayout());
		jTree = new JTree(new String[]{"first","second"});
		jTree.setRootVisible(true);
		jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		jTree.addMouseListener(this);
		JScrollPane scrollTree = new JScrollPane(jTree); 
		scrollTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jpanelFirst.add(scrollTree);
		
		popMenu = new JPopupMenu();
		JMenuItem copynodeItem = new JMenuItem("copy node");
		copynodeItem.addActionListener(this);
		JMenuItem copykeyItem = new JMenuItem("copy key");
		copykeyItem.addActionListener(this);
		JMenuItem copyvalueItem = new JMenuItem("copy value");
		copyvalueItem.addActionListener(this);
		JMenuItem copychildrenItem = new JMenuItem("copy children");
		copychildrenItem.addActionListener(this);
		popMenu.add(copynodeItem);
		popMenu.add(copykeyItem);
		popMenu.add(copyvalueItem);
		popMenu.add(copychildrenItem);
		// jTabbedpane.addTab(tabNames[i++],icon,creatComponent(),"first");//加入第一个页面
		jTabbedpane.addTab(tabNames[i++], null, jpanelFirst, "tree");// 加入第一个页面
		jTabbedpane.setMnemonicAt(0, KeyEvent.VK_0);// 设置第一个位置的快捷键为0

		// 第二个标签下的JPanel
		JPanel jpanelSecond = new JPanel();
		jpanelSecond.setLayout(new BorderLayout());
		area = new JTextArea(30, 200);
		Font font=new Font("宋体",Font.PLAIN,14);
		area.setFont(font);
		area.setLineWrap(true);
		
		area.setText("");
		JScrollPane scroll = new JScrollPane(area); 
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		jpanelSecond.add(scroll);
		
		jTabbedpane.addTab(tabNames[i++], null, jpanelSecond, "text");// 加入第一个页面
		jTabbedpane.setMnemonicAt(1, KeyEvent.VK_1);// 设置快捷键为1
		
		//url panel
		JPanel jpanelThird = new JPanel();
		jpanelThird.setLayout(null);
		//host
		JLabel hostLabel = new JLabel("host:");
		hostLabel.setBounds(20, 5, 40, 30);
		//JTextField hostField = new JTextField(50);
		//hostField.setBounds(65, 5, 700, 30);
		
		final DefaultComboBoxModel fruitsName = new DefaultComboBoxModel();

		//-Dhosts="http://cibn.api.3g.cp31.ott.cibntv.net,http://tv.api.3g.youku.com,http://10.10.72.63:9003,http://10.10.72.63:8003,http://play-ott.youku.taobao.com,http://127.0.0.1:8080,http://30.96.74.159"
		String hosts= System.getProperty("hosts");
		if(hosts!= null && !hosts.isEmpty()){
			Stream.of(hosts.split(",")).forEach(k->fruitsName.addElement(k));
		}
		
		 final JComboBox fruitCombo = new JComboBox(fruitsName);    
	     //fruitCombo.setSelectedIndex(0);

	      JScrollPane fruitListScrollPane = new JScrollPane(fruitCombo);  
	      
	      fruitListScrollPane.setBounds(65, 5, 700, 30);
		
		jpanelThird.add(hostLabel);
		jpanelThird.add(fruitListScrollPane);
		
		//path
		JLabel pathLabel = new JLabel("path:");
		pathLabel.setBounds(20, 40, 40, 30);
		JTextField pathField = new JTextField(50);
		pathField.setBounds(65, 40, 700, 30);		
		jpanelThird.add(pathLabel);
		jpanelThird.add(pathField);
		
		//ua
		JLabel uaLabel = new JLabel("ua:");
		uaLabel.setBounds(20, 75, 40, 30);
		JTextField uaField = new JTextField(50);
		uaField.setBounds(65, 75, 700, 30);		
		jpanelThird.add(uaLabel);
		jpanelThird.add(uaField);
		
		//cookie
		JLabel cookieLabel = new JLabel("cookie:");
		cookieLabel.setBounds(10, 110, 50, 30);
		JTextArea cookieField = new JTextArea(4,60);
		cookieField.setLineWrap(true);
		JScrollPane cookieTree = new JScrollPane(cookieField); 
		cookieTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		cookieTree.setBounds(65, 110, 700, 80);		
		jpanelThird.add(cookieLabel);
		jpanelThird.add(cookieTree);
		
		//head1
		JLabel h1keyLabel = new JLabel("head1key:");
		h1keyLabel.setBounds(5, 195, 70, 30);
		JTextField h1keyField = new JTextField(50);
		h1keyField.setBounds(80, 195, 70, 30);		
		
		JLabel h1valLabel = new JLabel("head1val:");
		h1valLabel.setBounds(155, 195, 70, 30);
		JTextField h1valField = new JTextField(50);
		h1valField.setBounds(230, 195, 535, 30);		
		jpanelThird.add(h1valLabel);
		jpanelThird.add(h1valField);
		
		jpanelThird.add(h1keyLabel);
		jpanelThird.add(h1keyField);
		
		//head2
		JLabel h2keyLabel = new JLabel("head2key:");
		h2keyLabel.setBounds(5, 230, 70, 30);
		JTextField h2keyField = new JTextField(50);
		h2keyField.setBounds(80, 230, 70, 30);		
		
		JLabel h2valLabel = new JLabel("head2val:");
		h2valLabel.setBounds(155, 230, 70, 30);
		JTextField h2valField = new JTextField(50);
		h2valField.setBounds(230, 230, 535, 30);		
		jpanelThird.add(h2valLabel);
		jpanelThird.add(h2valField);
		
		jpanelThird.add(h2keyLabel);
		jpanelThird.add(h2keyField);
		
		//request
		JButton requestButton = new JButton("request");
		requestButton.setBounds(200, 265, 100, 30);
		jpanelThird.add(requestButton);
		//reset
		JButton resetButton = new JButton("reset");
		resetButton.setBounds(400, 265, 100, 30);
		jpanelThird.add(resetButton);
		
		
		//request
		JLabel requestLabel = new JLabel("request:");
		requestLabel.setBounds(5, 300, 55, 30);
		JTextArea requestField = new JTextArea(4,60);
		//requestField.setLineWrap(true);
		JScrollPane requestTree = new JScrollPane(requestField); 
		requestTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		requestTree.setBounds(65, 300, 700, 100);		
		jpanelThird.add(requestLabel);
		jpanelThird.add(requestTree);
		
		//request
		JLabel responseLabel = new JLabel("response");
		responseLabel.setBounds(1, 405, 63, 30);
		JTextArea responseField = new JTextArea(4,60);
		//responseField.setLineWrap(true);
		JScrollPane responseTree = new JScrollPane(responseField); 
		responseTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		responseTree.setBounds(65, 405, 700, 140);		
		jpanelThird.add(responseLabel);
		jpanelThird.add(responseTree);
		
		jTabbedpane.addTab(tabNames[i++], null, jpanelThird, "url");// 加入第一个页面
		
		setLayout(new GridLayout(1, 1));
		add(jTabbedpane);
		
		requestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String url = (String) fruitCombo.getItemAt(fruitCombo.getSelectedIndex())+pathField.getText();
				String ua = uaField.getText();
				String cookie = cookieField.getText();
				Map<String, String> headers = new HashMap<>();
				if(null != cookie && !cookie.isEmpty())headers.put("Cookie", cookie);
				headers.put("User-Agent", ua);
				if(!h1valField.getText().isEmpty()) headers.put(h1keyField.getText(), h1valField.getText());
				if(!h2valField.getText().isEmpty()) headers.put(h2keyField.getText(), h2valField.getText());
				
				String[] resrps=HttpUtil.request(url, headers);
				requestField.setText(resrps[0]);
				responseField.setText(resrps[1]);
				String rsp = resrps[2];
				area.setText(rsp);
				jTabbedpane.setSelectedIndex(1);
				
			}
		});
		
		
		jTabbedpane.addChangeListener(new ChangeListener(){
			   public void stateChanged(ChangeEvent e){
			    JTabbedPane tabbedPane = (JTabbedPane)e.getSource();
			    int selectedIndex = tabbedPane.getSelectedIndex();
			    if(selectedIndex ==1){
			    	String text = area.getText();
			    	if(null != text && !"".equals(text)){
			    		text = Util.formatJson(text);
			    		area.setText(text);
			    	}
			    }else if(selectedIndex ==0){
			    	String text = area.getText();
			    	if(null != text && !"".equals(text)){
			    		JsonObject jsonObject = Util.toJson(text);
			    		showTree(jsonObject,jTree);
			    	}
			    }
			   }
			});
		
		//-DUA="Youku SmartTV PlayerSDK;1.8.0.000;Android;3.0.4-RS-20160608.1023;MagicBox_M13"
		String coo= System.getProperty("UA");
		if(null != coo && !coo.trim().isEmpty()) uaField.setText(coo);
		
	}
	
	public void showTree(JsonObject jsonObject,JTree jTree){
		
		DefaultMutableTreeNode node1= new DefaultMutableTreeNode("/-");
		genTree(jsonObject, node1);
		DefaultTreeModel model = new DefaultTreeModel(node1);
		jTree.setModel(model);
	}
	public void genTree(JsonElement jsonElement,DefaultMutableTreeNode node1){
		
		if(jsonElement.isJsonObject()){
			DefaultMutableTreeNode last =node1.getLastLeaf();
			String text = last.getUserObject().toString();
			boolean append=false;
			DefaultMutableTreeNode ne=null;
			if(text.endsWith("-")){
				last.setUserObject(text.substring(0, text.length()-1)+" : {");
				append = true;
			}else{
				ne=new DefaultMutableTreeNode("{");
				node1.add(ne);
			}
			
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()){
				String key = entry.getKey();
				JsonElement value = entry.getValue();
				DefaultMutableTreeNode n = new DefaultMutableTreeNode(key + "-");
				if(ne != null){
					ne.add(n);
				}else{
					node1.add(n);
				}
				genTree(value,n);
			}
			if(append)
			node1.add(new DefaultMutableTreeNode("}"));
			else ne.add(new DefaultMutableTreeNode("}"));
		}else if (jsonElement.isJsonArray()) {
			DefaultMutableTreeNode last =node1.getLastLeaf();
			String text = last.getUserObject().toString();
			DefaultMutableTreeNode ne=null;
			boolean append=false;
			if(text.endsWith("-")){
				last.setUserObject(text.substring(0, text.length()-1)+" : [");
				append = true;
			}else{
				ne=new DefaultMutableTreeNode("[");
				node1.add(ne);
			}
			JsonArray array = jsonElement.getAsJsonArray();
			for(int i=0;i<array.size();i++){
				JsonElement je = array.get(i);
				genTree(je, ne != null?ne:node1);
			}
			if(append)
			node1.add(new DefaultMutableTreeNode("]"));
			else ne.add(new DefaultMutableTreeNode("]"));
		}else{
			DefaultMutableTreeNode last =node1.getLastLeaf();
			String text = last.getUserObject().toString();
			if(text.endsWith("-")){
				last.setUserObject(text.substring(0, text.length()-1)+" : "+jsonElement);
			}else{
				node1.add(new DefaultMutableTreeNode(jsonElement));
			}
		}
	}

	public void mouseClicked(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		TreePath path = jTree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
		if (path == null) {
			return;
		}
		jTree.setSelectionPath(path);
		if (e.getButton() == 3) {
			popMenu.show(jTree, e.getX(), e.getY());
		}
	}



public void actionPerformed(ActionEvent e) {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
	String text = node.getUserObject().toString();
	String key=text;
	String value=text;
	if(text.contains(":")){
		int index = text.indexOf(":");
		if(index>0){
			key=text.substring(0, index);
			value=text.substring(index+1);
		}
	}
	if(e.getActionCommand().equals("copy node")){
		Util.setSysClipboardText(text);
	}else if (e.getActionCommand().equals("copy key")) {
		Util.setSysClipboardText(key);
	}else if (e.getActionCommand().equals("copy value")) {
		Util.setSysClipboardText(value);
	}else if (e.getActionCommand().equals("copy children")) {
		StringBuilder sb = new StringBuilder();
		sb.append(node.toString()+"\n");
		strTree(node,sb,0);
		Util.setSysClipboardText(sb.toString());
	}
	
}

public void strTree(TreeNode node,StringBuilder sb,int depth){
	depth++;
	for(int i=0;i<node.getChildCount();i++){
		TreeNode n = node.getChildAt(i);
		int len=depth;
		if(n.toString().equals("}")||n.toString().equals("]")){
			len--;
		}
		for(int j=0;j<len;j++){
			sb.append("\t");
		}
		if(i==node.getChildCount()-2 || n.toString().equals("{")||n.toString().equals("[")){
			sb.append(n+"\n");
		}else {
			sb.append(n+",\n");
		}
		
		
		strTree(n,sb,depth);
	}
}


	public static void main(String[] args) {
		JsonView view = new JsonView();
	}
}
