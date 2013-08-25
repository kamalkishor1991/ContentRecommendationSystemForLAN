package search;

import java.io.File;
import java.util.List;
/**
 * provide method {@link #getRecommendation()} and others to get List of Recommendation.
 * All Methods use Internet Media Type as video audio ebooks standard.
 * @author kamal
 *
 */
public interface Recommendation {
	/**
	 * 
	 * @return List of Recommended Items
	 */
	public List<File> getRecommendation();
	/**
	 * 
	 * @return Return Files Only.
	 */
	public List<File> getRecommendedFiles();
	/**
	 * 
	 * @return Return Folders Only.
	 */
	public List<File> getRecommendedFolders();
	/**
	 * 
	 * @return Recommended video only.
	 */
	public List<File> getRecommendedVideos();
	/**
	 * 
	 * @return PDF Files Only
	 */
	public List<File> getRecommendedEbooks();
	/**
	 * 
	 * @return MP3 and music files only.
	 */
	public List<File> getRecommendedMusic();
	
}
