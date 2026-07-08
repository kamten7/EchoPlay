import { api } from "./core";
import type { CategoryItem } from "@/types";

export const categoryService = {
  /** 加载所有分类（树形） */
  loadAllCategory() {
    return api.get<CategoryItem[]>("/category/loadAllCategory");
  },

  /** 加载父分类 */
  loadAllParentCategory() {
    return api.get<CategoryItem[]>("/category/loadAllParentCategory");
  },
};
