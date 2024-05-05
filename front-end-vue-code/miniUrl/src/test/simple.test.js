import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import home from '../components/home.vue'

describe('test app', () => {
  it('renders properly', () => {


    const wrapper = mount(home);
    console.log(wrapper.html());
  })
})
