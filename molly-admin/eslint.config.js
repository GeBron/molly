import pluginVue from 'eslint-plugin-vue'
import tsEslint from 'typescript-eslint'

export default [
  {
    ignores: ['dist', 'node_modules'],
  },
  ...tsEslint.configs.recommended,
  ...pluginVue.configs['flat/recommended'],
  {
    files: ['**/*.vue'],
    languageOptions: {
      parserOptions: {
        parser: tsEslint.parser,
      },
    },
  },
]
