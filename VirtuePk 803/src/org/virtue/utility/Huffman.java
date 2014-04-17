package org.virtue.utility;

import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.network.protocol.packet.RS3PacketReader;

public class Huffman {
	
	byte[] huffmanAlgorithm2;
    int[] huffmanAlgorithm1;
    int[] huffmanAlgorithm3;
	
	public Huffman (byte[] data) {
		int i = data.length;
		huffmanAlgorithm1 = new int[i];
		huffmanAlgorithm2 = data;
		int[] is_27_ = new int[33];
		huffmanAlgorithm3 = new int[8];
		int i_28_ = 0;
		for (int i_29_ = 0; i_29_ < i; i_29_++) {
		    int i_30_ = data[i_29_];
		    if (i_30_ != 0) {
			int i_31_ = 1 << 32 - i_30_;
			int i_32_ = is_27_[i_30_];
			huffmanAlgorithm1[i_29_] = i_32_;
			int i_33_;
			if (0 != (i_32_ & i_31_))
			    i_33_ = is_27_[i_30_ - 1];
			else {
			    i_33_ = i_32_ | i_31_;
			    for (int i_34_ = i_30_ - 1; i_34_ >= 1; i_34_--) {
				int i_35_ = is_27_[i_34_];
				if (i_35_ != i_32_)
				    break;
				int i_36_ = 1 << 32 - i_34_;
				if ((i_35_ & i_36_) != 0) {
				    is_27_[i_34_] = is_27_[i_34_ - 1];
				    break;
				}
				is_27_[i_34_] = i_35_ | i_36_;
			    }
			}
			is_27_[i_30_] = i_33_;
			for (int i_37_ = i_30_ + 1; i_37_ <= 32; i_37_++) {
			    if (i_32_ == is_27_[i_37_])
				is_27_[i_37_] = i_33_;
			}
			int i_38_ = 0;
			for (int i_39_ = 0; i_39_ < i_30_; i_39_++) {
			    int i_40_ = -2147483648 >>> i_39_;
			    if ((i_32_ & i_40_) != 0) {
				if (0 == huffmanAlgorithm3[i_38_])
				    huffmanAlgorithm3[i_38_] = i_28_;
				i_38_ = huffmanAlgorithm3[i_38_];
			    } else
				i_38_++;
			    if (i_38_ >= huffmanAlgorithm3.length) {
				int[] is_41_
				    = (new int
				       [huffmanAlgorithm3.length * 2]);
				for (int i_42_ = 0;
				     i_42_ < huffmanAlgorithm3.length;
				     i_42_++)
				    is_41_[i_42_]
					= huffmanAlgorithm3[i_42_];
				huffmanAlgorithm3 = is_41_;
			    }
			    i_40_ >>>= 1;
			}
			huffmanAlgorithm3[i_38_] = i_29_ ^ 0xffffffff;
			if (i_38_ >= i_28_)
			    i_28_ = i_38_ + 1;
		    }
		}
	}
	
	/**
	 * Encrypts the specified chat message and packs it into the stream
	 * @param stream	The stream to pack the message into
	 * @param message	The message to pack
	 * @return			The size of the encrypted message
	 */
	public int huffmanEncrypt(RS3PacketBuilder stream, String message) {
		int offset = 421967667 * stream.getPosition();
		byte[] msgData = getMessageData(message);
		stream.putPSmarts(msgData.length);
		stream.skip(encryptMessage(msgData, 0, msgData.length,
		    		stream.buffer(), stream.getPosition()));
		return stream.getPosition() - offset;
    }
	
	/**
	 * Decrypts the chat message contained within the specified stream
	 * @param stream	The stream containing the chat message
	 * @return			The decrypted message
	 */
	public String huffmanDecrypt(RS3PacketReader stream) {
    	return huffmanDecrypt(stream, 32767);
    }
    
	/**
	 * Decrypts the chat message contained within the specified stream
	 * @param stream	The stream containing the chat message
	 * @param maxSize	The maximum size for the decrypted message
	 * @return			The decrypted message
	 */
    public String huffmanDecrypt(RS3PacketReader buffer, int maxSize) {
		String output;
		try {
		    int encryptedLength = buffer.getSmart();
		    if (encryptedLength > maxSize) {
		    	encryptedLength = maxSize;
		    }
		    byte[] msgData = new byte[encryptedLength];
		    buffer.skip(decryptMessage(buffer.buffer(), buffer.getPosition(),
		    		msgData, 0, encryptedLength));
		    output = getMessageFromBytes(msgData, 0, encryptedLength);
		} catch (Exception exception) {
		    return "Cabbage";
		}
		return output;
    }

    int encryptMessage(byte[] messageData, int messageDataOffset, int messageDataLength, byte[] streamBuffer, int streamOffset) {
		int i_12_ = 0;
		int i_13_ = streamOffset << 3;
		for (messageDataLength += messageDataOffset; messageDataOffset < messageDataLength; messageDataOffset++) {
		    int value = messageData[messageDataOffset] & 0xff;
		    int codeword = huffmanAlgorithm1[value];
		    int codewordSize = huffmanAlgorithm2[value];
		    if (0 == codewordSize) {
		    	throw new RuntimeException(new StringBuilder().append("No codeword for data value: ").append(value).toString());
		    }
		    int streamPointer = i_13_ >> 3;
		    int i_18_ = i_13_ & 0x7;
		    i_12_ &= -i_18_ >> 31;
		    int i_19_ = streamPointer + (codewordSize + i_18_ - 1 >> 3);
		    i_18_ += 24;
		    streamBuffer[streamPointer] = (byte) (i_12_ |= codeword >>> i_18_);
		    if (streamPointer < i_19_) {
		    	streamPointer++;
				i_18_ -= 8;
				streamBuffer[streamPointer] = (byte) (i_12_ = codeword >>> i_18_);
				if (streamPointer < i_19_) {
					streamPointer++;
				    i_18_ -= 8;
				    streamBuffer[streamPointer] = (byte) (i_12_ = codeword >>> i_18_);
				    if (streamPointer < i_19_) {
				    	streamPointer++;
						i_18_ -= 8;
						streamBuffer[streamPointer] = (byte) (i_12_ = codeword >>> i_18_);
						if (streamPointer < i_19_) {
							streamPointer++;
						    i_18_ -= 8;
						    streamBuffer[streamPointer] = (byte) (i_12_ = codeword << -i_18_);
						}
				    }
				}
		    }
		    i_13_ += codewordSize;
		}
		return (7 + i_13_ >> 3) - streamOffset;
    }
    
    int decryptMessage(byte[] streamBuffer, int streamOffset, byte[] messageData, 
    		int messageDataOffset, int messagedDataLength) {
		if (0 == messagedDataLength) {
		    return 0;
		}
		int pointer = 0;
		messagedDataLength += messageDataOffset;
		int outputSize = streamOffset;
		for (;;) {
		    byte codeword = streamBuffer[outputSize];
		    if (codeword < 0) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    int value;
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    if ((codeword & 0x40) != 0) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    if ((codeword & 0x20) != 0) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    if (0 != (codeword & 0x10)) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    if ((codeword & 0x8) != 0) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    if (0 != (codeword & 0x4)) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    if ((codeword & 0x2) != 0) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    if (0 != (codeword & 0x1)) {
		    	pointer = huffmanAlgorithm3[pointer];
		    } else {
		    	pointer++;
		    }
		    if ((value = huffmanAlgorithm3[pointer]) < 0) {
		    	messageData[messageDataOffset++] = (byte) (value ^ 0xffffffff);
				if (messageDataOffset >= messagedDataLength) {
				    break;
				}
				pointer = 0;
		    }
		    outputSize++;
		}
		return outputSize + 1 - streamOffset;
    }
    
    public static byte[] getMessageData(CharSequence message) {
		int length = message.length();
		byte[] output = new byte[length];
		for (int index = 0; index < length; index++) {
		    char c = message.charAt(index);
		    if (c > 0 && c < '\u0080' || c >= '\u00a0' && c <= '\u00ff') {
		    	output[index] = (byte) c;
		    } else if (c == '\u20ac') {
		    	output[index] = (byte) -128;
		    } else if ('\u201a' == c) {
		    	output[index] = (byte) -126;
		    } else if ('\u0192' == c) {
		    	output[index] = (byte) -125;
		    } else if (c == '\u201e') {
		    	output[index] = (byte) -124;
		    } else if (c == '\u2026') {
		    	output[index] = (byte) -123;
		    } else if (c == '\u2020') {
		    	output[index] = (byte) -122;
		    } else if (c == '\u2021') {
		    	output[index] = (byte) -121;
		    } else if (c == '\u02c6') {
		    	output[index] = (byte) -120;
		    } else if (c == '\u2030') {
		    	output[index] = (byte) -119;
		    } else if ('\u0160' == c) {
		    	output[index] = (byte) -118;
		    } else if ('\u2039' == c) {
		    	output[index] = (byte) -117;
		    } else if (c == '\u0152') {
		    	output[index] = (byte) -116;
		    } else if ('\u017d' == c) {
		    	output[index] = (byte) -114;
		    } else if (c == '\u2018') {
		    	output[index] = (byte) -111;
		    } else if (c == '\u2019') {
		    	output[index] = (byte) -110;
		    } else if (c == '\u201c') {
		    	output[index] = (byte) -109;
		    } else if (c == '\u201d') {
		    	output[index] = (byte) -108;
		    } else if ('\u2022' == c) {
		    	output[index] = (byte) -107;
		    } else if (c == '\u2013') {
		    	output[index] = (byte) -106;
		    } else if (c == '\u2014') {
		    	output[index] = (byte) -105;
		    } else if ('\u02dc' == c) {
		    	output[index] = (byte) -104;
		    } else if ('\u2122' == c) {
		    	output[index] = (byte) -103;
		    } else if (c == '\u0161') {
		    	output[index] = (byte) -102;
		    } else if (c == '\u203a') {
		    	output[index] = (byte) -101;
		    } else if (c == '\u0153') {
		    	output[index] = (byte) -100;
		    } else if (c == '\u017e') {
		    	output[index] = (byte) -98;
		    } else if ('\u0178' == c) {
		    	output[index] = (byte) -97;
		    } else {
		    	output[index] = (byte) 63;
		    }
		}
		return output;
    }
    
    static char[] shiftCharacters = { '\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020',
	    '\u2021', '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0',
	    '\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c', '\u201d',
	    '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161',
	    '\u203a', '\u0153', '\0', '\u017e', '\u0178' };
    
    public static String getMessageFromBytes(byte[] messageData, int messageDataOffset, int messageDataLength) {
		char[] chars = new char[messageDataLength];
		int outputLen = 0;
		for (int index = 0; index < messageDataLength; index++) {
		    int character = messageData[index + messageDataOffset] & 0xff;
		    if (character != 0) {
				if (character >= 128 && character < 160) {
				    int newChar = shiftCharacters[character - 128];
				    if (0 == newChar) {
				    	newChar = 63;
				    }
				    character = newChar;
				}
				chars[outputLen++] = (char) character;
		    }
		}
		return new String(chars, 0, outputLen);
    }
}
