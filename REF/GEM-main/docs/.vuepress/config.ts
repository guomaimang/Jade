import { defineUserConfig } from "vuepress";
import theme from "./theme";

export default defineUserConfig({
  lang: "zh-CN",
  title: "GEM Project",
  description: "Mobile Computing Group Project Wiki",

  base: "/GEM",

  theme,
});
