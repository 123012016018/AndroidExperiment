# Notepad
## 主要功能
*  **添加时间显示**
* **添加基于标题的搜索功能**
## 附加功能
* **美化UI**
* **高亮搜索结果的关键字**
* **云备份和云同步**

---------------------------------------
### 一个非常重要的类NoteDAO，这是我自己写的一个封装了数据持久化的相关操作的类
```java
public class NoteDAO {
    private static final String TAG = "NOTEPAD";
    public static final String BACKUP_URL = "http://120.24.246.59:8888/backup";
    public static final String SYNCHRONIZATION_URL = "http://120.24.246.59:8888/query";
    private DatabaseHelper helper;
    private SQLiteDatabase readDB;
    private SQLiteDatabase writeDB;
    private Context context;

    public NoteDAO(Context context) {
        this.context = context;
        this.helper = new DatabaseHelper(context);
        readDB = helper.getReadableDatabase();
        writeDB = helper.getWritableDatabase();
    }

    //添加笔记到数据库中
    public void add(NoteVO noteVO) {
        String sql = "insert into "+TABLE_NAME+" ("+ID+","+TITLE+","+CONTENT+","+LAST_UPDATE+") values(?,?,?,?)";
        writeDB.execSQL(sql, new Object[]{noteVO.getId(),
                noteVO.getTitle(), noteVO.getContent(), noteVO.getLastUpdate()});
    }

    //根据笔记ID从数据库中删除
    public void delete(int id) {
        writeDB.delete(TABLE_NAME, ID+"=?", new String[]{id + ""});
    }


    //更新笔记
    public void update(NoteVO noteVO) {
        ContentValues values = new ContentValues();
        values.put(TITLE, noteVO.getTitle());
        values.put(CONTENT, noteVO.getContent());
        values.put(LAST_UPDATE,noteVO.getLastUpdate());
        writeDB.update(TABLE_NAME, values, ID+"=?", new String[]{noteVO.getId() + ""});
    }

    //获取数据库中的所有笔记
    public List<NoteVO> listAll() {
        String sql = "select * from " + TABLE_NAME;
        return listAll(sql,null);
    }
    
    //获取数据库中满足某些条件的所有笔记
    public List<NoteVO> listAll(String sql,String[] args){
        List<NoteVO> list = new ArrayList<>();
        Cursor cursor = readDB.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            NoteVO noteVO = new NoteVO();
            noteVO.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            noteVO.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            noteVO.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
            noteVO.setLastUpdate(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)));
            list.add(noteVO);
        }
        return list;
    }

    //根据标题进行模糊搜索
    public List<NoteVO> search(String keyword){
        String sql = "select * from "+TABLE_NAME+" where title like '%"+keyword+"%'";
        return listAll(sql,null);
    }

    //云备份，将数据库中所有的笔记同步到我的阿里云
    public void backup() {
        List<NoteVO> list = this.listAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String value = mapper.writeValueAsString(list);
            Log.d(TAG, value);
            Map<String, String> map = new HashMap<>();
            map.put("data", value);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpUtils.postData(BACKUP_URL,map);
                }
            });
            t.start();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    //云同步,将我的阿里云上数据库中的笔记同步到本地
    public void synchronization() {
        writeDB.delete(TABLE_NAME, null, null);
        String html = HttpUtils.getHtml(SYNCHRONIZATION_URL);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode readTree = mapper.readTree(html);
            Iterator<JsonNode> elements = readTree.elements();
            NoteDAO dao = new NoteDAO(NoteDAO.this.context);
            while (elements.hasNext()) {
                JsonNode node = elements.next();
                NoteVO noteVO = mapper.readValue(node.toString(), NoteVO.class);
                dao.add(noteVO);
            }
            dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //释放相关数据库连接资源
    public void close(){
        readDB.close();
        writeDB.close();
        helper.close();
    }

```
### 添加时间显示
* **首先在list_item.xml中添加显示时间的TextView**
```java
        <TextView
            android:id="@+id/item_tv_date"
            android:textSize="15sp"
            android:text="2019-5-6 12:35"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
        
```
* **格式化时间戳**
```java
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
   noteVO.setLastUpdate(sdf.format(new Date()));
```

*功能实现后如下图所示：*
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/date.png)

### 添加基于标题的搜索功能
* **首先在activity_main.xml中添加一个搜索框 EditText**
```java
<EditText
        android:id="@+id/et_search"
        android:padding="5dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="8dp"
        android:hint="@string/et_search"
        android:background="#fff"
        android:inputType="text"
        android:textSize="25sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:importantForAutofill="no" />
```
* **对搜索框添加键盘监听事件,监听回车键按下**
```java
etSearch = findViewById(R.id.et_search);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //判断回车键是否被按下
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String keyword = etSearch.getText().toString();
                    NoteDAO dao = new NoteDAO(MainActivity.this);
                    List<NoteVO> list = dao.search(keyword);
                    adapter.setKeyword(list, keyword);
                    adapter.notifyDataSetChanged();
                    dao.close();
                    return true;
                }
                return false;
            }
        });
```
* **编写基本标题搜索的SQL语句与实现**
```
String sql = "select * from "+TABLE_NAME+" where title like '%"+keyword+"%'";
```
* **搜索功能完成后的效果图如下:**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/search.png)

### 美化UI
* **重新设计了记事本的应用布局,使得看上去更美观**
* **主界面效果图:**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/main.png)
* **笔记编辑界面:**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/addNote.png)
* **笔记删除按钮与提示:**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/deleteNote.png)

### 高亮搜索结果的关键字
* **关键代码如下:**
```java
 /**
     * 
     * @param text 关键字
     * @param target 需要匹配的目标文本
     * @param color 关键字的颜色
     * @return
     */
    private static SpannableString highlight(String text, String target,
                                            String color, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
            spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
```

* **将SpannableString对象设置为TextView的text**
```java
tvTitle.setText(highlight(noteVO.getTitle(),keyword,"#EA2D2D",0,0));
```
设置完后,TextView中的关键字就会高亮显示
* **效果如下:**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/highlight.png)

### 云备份
首先创建一个web应用，提供两个REST API接口<br>
一个实现备份，一个实现同步。(其实就是两个URL)<br>
规定数据的提交与返回 都统一用json格式的数据(我使用jackson实现json数据的序列化和反序列化)
* **使用OKHttp实现HTTP请求**
```java
public class HttpUtils {
    //获取url的页面数据
    public static String getHtml(String url) {
        String result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    //将数据post到服务器上
    public static void postData(String url, Map<String, String> map) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.add(key, map.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
* **从本地数据库中获取笔记内容，然后格式化成json字符串，再提交到服务器上**
```java
 //云备份，将数据库中所有的笔记同步到我的阿里云
    public void backup() {
        List<NoteVO> list = this.listAll();//从数据库中获取所有笔记
        ObjectMapper mapper = new ObjectMapper();
        try {
            String value = mapper.writeValueAsString(list);//序列化成json字符串
            Log.d(TAG, value);//日志
            Map<String, String> map = new HashMap<>();
            map.put("data", value);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //将数据提交到服务器上
                    HttpUtils.postData(BACKUP_URL, map);
                }
            });
            t.start();//开启一个后台线程进行数据的提交
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
```
* **截图**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/backup.png)

### 同步
* **从服务器获取数据然后反序列化成NoteVO对象保存到本地**
```java
 //云同步,将我的阿里云上数据库中的笔记同步到本地
    public void synchronization() {
        //清空本地数据
        writeDB.delete(TABLE_NAME, null, null);
        String html = HttpUtils.getHtml(SYNCHRONIZATION_URL);
        ObjectMapper mapper = new ObjectMapper();
        //反序列化
        try {
            JsonNode readTree = mapper.readTree(html);
            Iterator<JsonNode> elements = readTree.elements();
            NoteDAO dao = new NoteDAO(NoteDAO.this.context);
            while (elements.hasNext()) {
                JsonNode node = elements.next();
                NoteVO noteVO = mapper.readValue(node.toString(), NoteVO.class);
                dao.add(noteVO);//将服务器上的数据保存到本地
            }
            dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
```

* **同步前:**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/before.png)
* **同步后:**
![](https://github.com/123012016018/AndroidExperiment/blob/master/NotePad/screenshot/after.png)
