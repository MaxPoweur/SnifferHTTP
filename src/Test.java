import org.apache.commons.lang3.StringUtils;

public class Test {
	public static void main(String[] args) throws Exception
	{
		String[] s = StringUtils.substringsBetween("tagabctagettagabctag", "tag", "tag");
		for(int i=0; i<s.length; i++)
		{
			 System.out.println(s[i]);
			
		}
    }
}
