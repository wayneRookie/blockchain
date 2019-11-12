package utility;

import java.math.BigInteger;

public class ProofOfWork {
	private String block;
	private int diff;// 难度
	/*
	 * 初始化工作量证明算法
	 */

	public ProofOfWork(String block, int diff) {
		this.block = block;
		this.diff = diff;
	}

	public String[] pow() {
		String[] res = new String[2];
		BigInteger current = new BigInteger(SHA256.getSHA256(block), 16);//将hash的十六进制转换为十进制的大数
		BigInteger uptarget = new BigInteger("1", 2);//定义一个大数1
		uptarget = uptarget.shiftLeft(256 - diff * 4);//目标数左移，保证前导零个数
		BigInteger downtarget = new BigInteger("1", 2);//定义一个大数1
		downtarget = downtarget.shiftLeft(256 - diff * 4-1);
		int nonce = 0;
		String sha = "";
		while (!(current.compareTo(uptarget) == -1)||(current.compareTo(downtarget)==-1)) {
			sha = SHA256.getSHA256(block + nonce++);
			current = new BigInteger(sha.toString(), 16);
		}
		res[0] = sha;
		res[1] = String.valueOf(nonce - 1);
		return res;
	}

}
