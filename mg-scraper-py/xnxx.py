# As its name, it scrape some stuff.
# Copyright (C) 2026  kunteinzo
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.


from requests import get
from bs4 import BeautifulSoup
from re import compile, match, findall
from json import dumps

headers = {'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.32 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.32'}


__domain__ = 'https://www.xnxx.com/'

_mode = ['hits', 'random']
_period = ['year', 'month']
_length = ['0-10min', '10min+', '10-20min', '20min+']
_quality = ['hd-only', 'fullhd']


def search(q: str, mode: str = '', period: str = '', length: str = '', quality: str = ''):
    data = dict(
        pages=None,
        items=[]
    )
    
    _q = q
    if not q.startswith('/search/'):
        q = f'/search/{q}'
    url = __domain__
    if mode in _mode:
        url += f'{mode}/'
    if period in _period:
        url += f'{period}/'
    if length in _length:
        url += f'{length}/'
    if quality in _quality:
        url += f'{quality}/'
    rp = BeautifulSoup(get(f'{url}{q}', headers=headers).content, 'html.parser')
    
    # find latest page
    lp = rp.find('div', class_='pagination')
    if lp:
        lp = lp.find('a', class_='last-page')['href']
        lp = int(lp[lp.rindex('/')+1:])
        data['pages'] = lp
    
    # extract data
    for s in rp.find_all('div', class_='thumb-block'):
        _s = dict(
            maker_name=None, maker_link=None,
            title=None, link=None,
            src=None, sfwsrc=None, pvv=None, mzl=None)
        if not s: continue
        if maker := s.find('a', href=compile('/porn-maker/')):
            _s['maker_name'] = maker.text.strip()
            _s['maker_link'] = maker['href']
        if video := s.find('a', href=compile('/video-'), title=compile('.*')):
            _s['title'] = video['title']
            _s['link'] = video['href']
        if img := s.find('img', id='pic_'+s['data-id']):
            _s['src']=img['data-src'] if img.has_attr('data-src') else None
            _s['sfwsrc']=img['data-sfwthumb'] if img.has_attr('data-sfwthumb') else None
            _s['pvv']=img['data-pvv'] if img.has_attr('data-pvv') else None
            _s['mzl']=img['data-mzl'] if img.has_attr('data-mzl') else None
        data['items'].append(_s)
    return data


def load_video(url: str):
    rp = BeautifulSoup(get(f'{__domain__}{url}').content, 'html.parser')
    meta = rp.find('div', id='video-content-metadata')
    title = meta.find('div', class_='video-title').find('strong').text.strip()
    desc = meta.find('p', class_='video-description').text.strip()
    link = meta.find('input', id='copy-video-link')['value']
    embed = meta.find('input', id='copy-video-embed')['value']
    print(link)
    print(embed)
    src = rp.find('script', crossorigin='anonymous', string=compile('html5player'))
    # print(findall(r'VideoHLS.*\'|ThumbUrl.*\'|ThumbUrl169.*\'|ThumbSlide.*\'', src.text))
    


if __name__ == '__main__':
    # print(dumps(search('japan'), ensure_ascii=False, indent=2))
    load_video('/video-v2akzaa/pretty_japanese_girl_fucked_by_old_guy')
    exit()
    rp = BeautifulSoup(get('https://www.xnxx.com/search/japan', headers=headers).content, 'html.parser')
    months = [dict(title=a['href'].replace('-', ' ').replace('/best/', ''), link=a['href'])
        for a in rp.find_all('a', class_='month-label')]
    print(len(months))
    for s in rp.find_all('div', class_='thumb-block'):
        _id = s['data-id']
        maker = s.find('a', href=compile('/porn-maker'))
        video = s.find('a', href=compile('/video-'), title=compile('.+'))
        img = s.find('img', id=f'pic_{_id}')
        if maker:
            print('Maker', maker.text)
        print('Video', video.text)
        print(img['data-src'])
        print(img['data-sfwthumb'])
        print(img['data-pvv'])
        print(img['data-mzl'])
        break
    lp = rp.find('div', class_='pagination')
    lp = lp.find('a', class_='last-page')['href']
    lp = int(lp[lp.rindex('/')+1:])
    print(lp)
    
