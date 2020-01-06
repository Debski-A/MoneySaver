import i18n from "i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import { initReactI18next } from "react-i18next";
import navigation_en from '../translations/navigation_en.json'
import navigation_pl from '../translations/navigation_pl.json'
import registerPage_pl from '../translations/registerPage_pl'
import registerPage_en from '../translations/registerPage_en'
import loginPage_en from '../translations/loginPage_en'
import loginPage_pl from '../translations/loginPage_pl'
import homepage_pl from '../translations/homepage_pl'
import homepage_en from '../translations/homepage_en'
import addIncomePage_pl from '../translations/addIncomePage_pl'
import addIncomePage_en from '../translations/addIncomePage_en'
import addOutcomePage_pl from '../translations/addOutcomePage_pl'
import addOutcomePage_en from '../translations/addOutcomePage_en'

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    // we init with resources
    resources: {
      en: {
        navigation: navigation_en,
        register_page: registerPage_en,
        login_page: loginPage_en,
        homepage: homepage_en,
        add_income_page: addIncomePage_en,
        add_outcome_page: addOutcomePage_en,
        translations: {
          //default translation will go here
        }
      },
      pl: {
        navigation: navigation_pl,
        register_page: registerPage_pl,
        login_page: loginPage_pl,
        homepage: homepage_pl,
        add_income_page: addIncomePage_pl,
        add_outcome_page: addOutcomePage_pl,
        translations: {
          //default translation will go here
        }
      }
    },
    fallbackLng: "en",
    debug: true,

    // have a common namespace used around the full app
    ns: ["translations"],
    defaultNS: "translations",

    keySeparator: false, // we use content as keys

    interpolation: {
      escapeValue: false
    }
  });
  
export default i18n;
