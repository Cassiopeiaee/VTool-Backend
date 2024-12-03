
export default {
  bootstrap: () => import('./main.server.mjs').then(m => m.default),
  inlineCriticalCss: true,
  routes: [
  {
    "renderMode": 2,
    "route": "/"
  },
  {
    "renderMode": 2,
    "route": "/weiterlesen"
  }
],
  assets: new Map([
['index.csr.html', {size: 505, hash: '79143be497e7f6522b17576bf806de568586b38d2780e9019960e9d3f44ef8a1', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)}], 
['index.server.html', {size: 1018, hash: '7dbb471d2f22280a55da96d9e056de08172fd81b0d98e84478ac97a7051b41fa', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)}], 
['index.html', {size: 1347, hash: '7cf6f0443d60c072a13170b029495923da290e61487822edd478782083a049f6', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)}], 
['weiterlesen/index.html', {size: 1347, hash: '7cf6f0443d60c072a13170b029495923da290e61487822edd478782083a049f6', text: () => import('./assets-chunks/weiterlesen_index_html.mjs').then(m => m.default)}], 
['styles-5INURTSO.css', {size: 0, hash: 'menYUTfbRu8', text: () => import('./assets-chunks/styles-5INURTSO_css.mjs').then(m => m.default)}]
]),
  locale: undefined,
};
