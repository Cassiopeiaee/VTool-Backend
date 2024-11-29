
export default {
  bootstrap: () => import('./main.server.mjs').then(m => m.default),
  inlineCriticalCss: true,
  routes: [
  {
    "renderMode": 2,
    "route": "/"
  }
],
  assets: new Map([
['index.csr.html', {size: 505, hash: '63135c8914349880d16d3740ea84d8f52fcf68efcf10f441c12b5cdc92f2c8a4', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)}], 
['index.server.html', {size: 1018, hash: '3b3c1f82adba5635f7e5617d27cd84fe55385348df7d3e2dc22df38a20f38e8e', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)}], 
['index.html', {size: 20832, hash: '749076c8d45b732cd3ff011fca5e73dc4e2b10ddf27ddb00a97681498a5aadf8', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)}], 
['styles-5INURTSO.css', {size: 0, hash: 'menYUTfbRu8', text: () => import('./assets-chunks/styles-5INURTSO_css.mjs').then(m => m.default)}]
]),
  locale: undefined,
};
