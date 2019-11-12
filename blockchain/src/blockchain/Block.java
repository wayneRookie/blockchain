package blockchain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;

import utility.ProofOfWork;

public class Block implements Serializable {
	private int height;// 区块高度
	private String prehash;// 前一个区块的hash值
	private String data;// 区块中包含的数据
	private String timestamp;// 时间戳
	private String hash;// 当前区块hash值
	private int nonce;// 随机值

	/**
	 * 构造方法生成一个新的区块
	 */
	public Block(String data, String prehash, int height) {
		this.height = height;
		this.data = data;
		this.prehash = prehash;
		this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Instant.now().toEpochMilli());
				Instant.now().toEpochMilli();
		String[] pow = new ProofOfWork(new StringBuffer(height + data + prehash + timestamp).toString(), 2).pow();
		this.hash = pow[0];
		this.nonce = Integer.parseInt(pow[1]);
	}

	public String getHash() {
		return hash;
	}

	public int getHeight() {
		return height;
	}
	
	public String getPrehash() {
		return prehash;
	}
	
	public String getData() {
		return data;
	}
	
	public int getNonce() {
		return nonce;
	}
	
	public String getTimestamp() {
		return timestamp;
	}

}
