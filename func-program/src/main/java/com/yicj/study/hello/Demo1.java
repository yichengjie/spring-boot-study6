package com.yicj.study.hello;

import java.util.ArrayDeque;
import java.util.Queue;


public class Demo1 {
	public static void main(String[] args) {
		TreeNode root = new TreeNode("1");
		root.left = new TreeNode("l-2");
		root.right = new TreeNode("r-2");
		root.left.left = new TreeNode("l-l-3");
		root.left.right = new TreeNode("l-r-3");
		root.right.left = new TreeNode("r-l-3");
		root.right.right = new TreeNode("r-r-3");
		root.left.left.left = new TreeNode("l-l-l-4");
		root.left.left.right = new TreeNode("l-l-r-4");
		root.left.right.left = new TreeNode("l-r-l-4");
		root.right.left.left = new TreeNode("r-l-l-4");
		root.left.right.right = new TreeNode("l-r-r-4");
		
		System.out.println(getMaxDepth(root));
		System.out.println(getMaxWidth(root));
	}
	
	//递归获取二叉树的最大深度
	public static int getMaxDepth(TreeNode root) {
		if(root==null) {
			return 0;
		}else {
			int left = getMaxDepth(root.left);
			int rigth = getMaxDepth(root.right);
			return 1+Math.max(left, rigth);
		}
	}
	
	//获取二叉树宽度,使用到队列
	public static int getMaxWidth(TreeNode root) {
		if(root == null) {
			return 0;
		}else {
			Queue<TreeNode> queue = new ArrayDeque<TreeNode>();//定义一个队列
			int maxWidth = 1;//最大宽度
			queue.add(root);//入队
			while(true) {
				int len = queue.size();//当前层节点个数
				if(len == 0) {
					break;
				}else {
					while(len>0) {//如果当前层还有节点
						TreeNode t = queue.poll();//拿到队列头部第一个节点并删除
						len--;
						if(t.left!=null) {
							queue.add(t.left);//下一次节点入队
						}
						if(t.right!=null) {
							queue.add(t.right);//下一节点入队
						}
					}
					maxWidth = Math.max(maxWidth, queue.size());
				}
			}
			return maxWidth;
		}
	}

	static class TreeNode{
		String val;
		TreeNode left = null;
		TreeNode right = null;
		TreeNode(String val){
			this.val = val;
		}
	}
}