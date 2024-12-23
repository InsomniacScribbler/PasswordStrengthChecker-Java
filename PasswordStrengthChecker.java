import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PasswordStrengthChecker {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a password to check its strength:");
		String password = scanner.nextLine();

		PasswordFeedback result = evaluatePasswordStrength(password);

		System.out.println("\nPassword Strength Feedback:");
		for (String comment : result.feedback) {
			System.out.println("- " + comment);
		}
		System.out.println("Normalized Score: " + result.normalizedScore + "/100");
		System.out.println("Overall password strength: " + result.strengthCategory);
	}

	public static PasswordFeedback evaluatePasswordStrength(String password) {
		int maxScore = 100;
		double score = 0.0;
		HashSet<String> feedback = new HashSet<>();

		// Length factor (weighted 30%)
		int length = password.length();
		double lengthScore;
		if (length >= 12) {
			lengthScore = 30.0; // Full marks for excellent length
			feedback.add("Password length is excellent.");
		} else if (length >= 8) {
			lengthScore = 20.0; // Partial marks for acceptable length
			feedback.add("Password length is acceptable.");
		} else {
			lengthScore = 10.0; // Low score for weak length
			feedback.add("Password is too short. Minimum length is 8 characters.");
		}
		score += lengthScore;

		// Complexity factor (weighted 50%)
		double complexityScore = 0.0;
		if (Pattern.compile("[a-z]").matcher(password).find()) {
			complexityScore += 12.5; // Partial weight for lowercase
		} else {
			feedback.add("Password should include at least one lowercase letter.");
		}

		if (Pattern.compile("[A-Z]").matcher(password).find()) {
			complexityScore += 12.5; // Partial weight for uppercase
		} else {
			feedback.add("Password should include at least one uppercase letter.");
		}

		if (Pattern.compile("[0-9]").matcher(password).find()) {
			complexityScore += 12.5; // Partial weight for digits
		} else {
			feedback.add("Password should include at least one digit.");
		}

		if (Pattern.compile("[@$!%*?&]").matcher(password).find()) {
			complexityScore += 12.5; // Partial weight for special characters
		} else {
			feedback.add("Password should include at least one special character (@$!%*?&).");
		}
		score += complexityScore;

		// Uniqueness factor (weighted 20%)
		HashSet<Character> uniqueChars = new HashSet<>();
		for (char c : password.toCharArray()) {
			uniqueChars.add(c);
		}
		double uniquenessRatio = (double) uniqueChars.size() / length;
		double uniquenessScore;
		if (uniquenessRatio > 0.7) {
			uniquenessScore = 20.0; // Full marks for good uniqueness
			feedback.add("Password has a good level of uniqueness.");
		} else {
			uniquenessScore = 10.0; // Partial marks for weak uniqueness
			feedback.add("Password could be more unique. Avoid using repetitive characters.");
		}
		score += uniquenessScore;

		// Normalized Score and Strength Category
		double normalizedScore = Math.min(score, maxScore); // Cap the score at 100
		String strengthCategory = determineStrengthCategory(normalizedScore);

		return new PasswordFeedback(normalizedScore, feedback, strengthCategory);
	}

	private static String determineStrengthCategory(double score) {
		if (score >= 80) {
			return "Strong";
		} else if (score >= 50) {
			return "Moderate";
		} else {
			return "Weak";
		}
	}
}

class PasswordFeedback {
	double normalizedScore;
	HashSet<String> feedback;
	String strengthCategory;

	public PasswordFeedback(double normalizedScore, HashSet<String> feedback, String strengthCategory) {
		this.normalizedScore = normalizedScore;
		this.feedback = feedback;
		this.strengthCategory = strengthCategory;
	}
}
