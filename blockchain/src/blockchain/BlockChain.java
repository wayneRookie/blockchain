package blockchain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utility.BerkeleyDB;
import utility.SerializeAndDeserialize;

/*
 * 定义一个区块链类型
 * */
public class BlockChain {
	/*
	 * 初始化BerkeleyDB来存储区块链，并初始化创世区块
	 */
	private BerkeleyDB dbUtil = new BerkeleyDB("/Users/wayne/Documents/blockchain");
	private final String genesisHash = "0000000000000000000000000000000000000000000000000000000000000000";
	public BlockChain() throws IOException {
		String pre = genesisHash;
		String data = "Genesis block";
		Block block = new Block(data, pre, 1);
		dbUtil.writeToDatabase(block.getHash(), SerializeAndDeserialize.serializeToString(block), false);
		dbUtil.writeToDatabase("current_hash", block.getHash(), true);// 用来不断更新当前区块链的最后一个区块的哈希值。
	}

	/*
	 * 用来读取区块链信息
	 */
	public List<Block> getBlockchain() throws ClassNotFoundException, IOException {
		List<Block> blc = new ArrayList<>();
		String cur = dbUtil.readFromDatabase("current_hash");
		while(!cur.equals(genesisHash)) {
			Block block = (Block)SerializeAndDeserialize.deserializeToObject(dbUtil.readFromDatabase(cur));
			blc.add(block);
			cur = block.getPrehash();
		}
		return blc;
	}

	/*
	 * 创建一个新的区块并添加到区块链中
	 */
	public void createNewBlockAndToBlockchain(String data) throws ClassNotFoundException, IOException {
		//blc.add(new Block(data, blc.get(blc.size() - 1).getHash(), blc.get(blc.size() - 1).getHeight() + 1));
		String cur = dbUtil.readFromDatabase("current_hash");
		Block block = (Block)SerializeAndDeserialize.deserializeToObject(dbUtil.readFromDatabase(cur));
		Block newBlock = new Block(data,block.getHash(),block.getHeight()+1);
		dbUtil.writeToDatabase(newBlock.getHash(), SerializeAndDeserialize.serializeToString(newBlock), false);
		dbUtil.writeToDatabase("current_hash", newBlock.getHash(),true);
		dbUtil.closeDB();
	}
}
