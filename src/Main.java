import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import com.kylc.torrent.io.BitTorrentFile;
import com.kylc.torrent.io.bencode.BencodeByteStringType;
import com.kylc.torrent.io.bencode.BencodeDictionaryType;
import com.kylc.torrent.io.bencode.BencodeDictionaryType.BencodeDictionaryElement;
import com.kylc.torrent.io.bencode.BencodeListType;
import com.kylc.torrent.io.bencode.BencodeType;

public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length != 1 || !args[0].endsWith(".torrent")) {
			System.err.println("need input torrent file!");
			System.exit(1);
		}
		String bitFileName = args[0];
		BitTorrentFile bitFile = BitTorrentFile.open(bitFileName);
		BencodeDictionaryType root = bitFile.getRoot();

		// modify all byte string of torrent name and files name
		BencodeDictionaryType info = bitFile.getInfo();
		String originName = ((BencodeByteStringType) info.lookup("name.utf-8"))
				.getString();
		System.out.println("Origin name: " + originName);
		System.out.print("Please set new name: ");
		Scanner scanner = new Scanner(System.in);
		String newName = scanner.next();

		byte[] bytes = newName.getBytes();
		((BencodeByteStringType) info.lookup("name.utf-8")).setStringType(
				bytes.length, bytes);

		List<BencodeType> files = ((BencodeListType) info.lookup("files"))
				.getNativeValue();
		int count = 0;
		for (BencodeType file : files) {
			count++;
			BencodeByteStringType byteString = (BencodeByteStringType)(((BencodeListType)((BencodeDictionaryType) file).lookup("path.utf-8")).getNativeValue().get(0));
			String fileName = byteString.getString();
			String newFileName = ""+count+fileName.substring(fileName.lastIndexOf("."));
			//System.out.println(newFileName);
			bytes = newFileName.getBytes();
			byteString.setStringType(bytes.length, bytes);
		}

		PrintStream out = new PrintStream(bitFileName.substring(0,
				bitFileName.length() - 8)
				+ "_harm.torrent");
		root.dump(out);
		System.out.println("OK!");
	}
}
