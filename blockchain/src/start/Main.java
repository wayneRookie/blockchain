package start;

import java.io.IOException;
import java.util.List;

import blockchain.Block;
import blockchain.BlockChain;

public class Main {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		BlockChain blc = new BlockChain();
		
		for (int i = 0; i < 10; i++) {
			blc.createNewBlockAndToBlockchain("这是一个测试区块"+i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<Block> list = blc.getBlockchain();
		for(Block b : list) {
			System.out.println("hash："+b.getHash()
			+" 高度："+b.getHeight()
			+" prehash值"+b.getPrehash()
			+" data值为："+b.getData()
			+" 时间戳："+b.getTimestamp()
			+" nonce值："+b.getNonce());
		}
	}
}
