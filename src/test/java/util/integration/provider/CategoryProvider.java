/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.integration.provider;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import util.integration.domain.Category;

/**
 *
 * @author Michael Koppen
 */
@Stateful
public class CategoryProvider {

	List<Category> catList;

	public CategoryProvider() {
		catList = new ArrayList<Category>();
	}

	public List<Category> getAll() {
		return catList;
	}

	public int count() {
		return catList.size();
	}

	public boolean add(Category e) {
		return catList.add(e);
	}

	public Category get(int index) {
		return catList.get(index);
	}

	public Category remove(int index) {
		return catList.remove(index);
	}

}
