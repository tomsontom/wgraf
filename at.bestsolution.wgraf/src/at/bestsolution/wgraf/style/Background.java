package at.bestsolution.wgraf.style;


public abstract class Background {

	public abstract byte[] getHash();
	
	public String getHexHash() {
		byte[] mdbytes = getHash();
		 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
		return sb.toString();
	}
}
