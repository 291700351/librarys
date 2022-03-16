package io.github.lee.core.databinding.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import io.github.lee.core.databinding.ui.callback.PageCreatorInterface
import io.github.lee.core.databinding.ui.callback.PageMethodInterface
import io.github.lee.core.databinding.ui.callback.PageStatusOperateInterface
import io.github.lee.core.databinding.ui.databinding.LayoutBaseHoverBinding
import io.github.lee.core.databinding.ui.databinding.LayoutBaseLinearBinding
import io.github.lee.core.vm.BaseViewModel
import io.github.lee.core.vm.exceptions.ResultThrowable
import io.github.lee.core.vm.exceptions.normalThrowable
import io.github.lee.core.vm.status.PageStatus
import kotlinx.coroutines.flow.collect

open class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    PageStatusOperateInterface, PageMethodInterface<VM>, PageCreatorInterface<VB> {

    protected val vm by lazy {
        onCreateViewModel()?.also {
            lifecycle.addObserver(it)
        }
    }

    protected val mContext: Context by lazy { this }
    private lateinit var mToast: Toast

    /**初始化指定当前界面Toolbar是线性排列还是悬浮在界面*/
    protected open var mToolbarHover = false

    private lateinit var mToolbarContainer: FrameLayout
    private lateinit var mContentContainer: FrameLayout

    protected var mToolbar: Toolbar? = null
        private set
    protected var mLoadingVb: ViewDataBinding? = null
        private set
    protected var vb: VB? = null
        private set
    protected var mEmptyVb: ViewDataBinding? = null
        private set
    protected var mErrorVb: ViewDataBinding? = null
        private set

    //===Desc:=====================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitData(savedInstanceState)
        if (isFinishing) {
            return
        }
        hideActionbar()
        //创建Toast
        @SuppressLint("ShowToast")
        mToast = onCreateToast() ?: Toast.makeText(mContext, "", Toast.LENGTH_SHORT)
        //设置界面显示
        val rootView = if (mToolbarHover) {
            val view = LayoutBaseHoverBinding.inflate(layoutInflater)
            mToolbarContainer = view.flToolbarContainer
            mContentContainer = view.flContentContainer
            view
        } else {
            val view = LayoutBaseLinearBinding.inflate(layoutInflater)
            mToolbarContainer = view.flToolbarContainer
            mContentContainer = view.flContentContainer
            view
        }
        mToolbarContainer.removeAllViews()
        mToolbarContainer.visibility = View.GONE
        mContentContainer.removeAllViews()

        mToolbar = onCreateToolbar()?.also {
            mToolbarContainer.addView(it)
            it.title = ""
            setSupportActionBar(it)
            mToolbarContainer.visibility = View.VISIBLE
            if (mToolbarHover) {
                mToolbarContainer.fitsSystemWindows = true
            }
        }

        mLoadingVb = onCreateLoading()?.also {
            it.lifecycleOwner = this
            mContentContainer.addView(it.root)
        }

        vb = onCreateSuccess()?.also {
            it.lifecycleOwner = this
            mContentContainer.addView(it.root)
        }
        mEmptyVb = onCreateEmpty()?.also {
            it.lifecycleOwner = this
            mContentContainer.addView(it.root)
        }
        mErrorVb = onCreateError()?.also {
            it.lifecycleOwner = this
            mContentContainer.addView(it.root)
        }
        setContentView(rootView.root)
        //根据子类创建的界面对象  显示不同状态
        if (null != mLoadingVb) {
            showLoading()
        } else {
            if (null != vb) {
                showSuccess()
            } else {
                if (null != mEmptyVb) {
                    showEmpty(normalThrowable(getString(R.string.core_data_empty)))
                } else {
                    if (null != mErrorVb) {
                        showError(normalThrowable(getString(R.string.core_data_error)))
                    }
                }
            }
        }

        onObserve()
        onSetViewListener()
        onSetViewData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoadingVb?.unbind()
        vb?.unbind()
        mEmptyVb?.unbind()
        mErrorVb?.unbind()
        if (null != vm) {
            lifecycle.removeObserver(vm!!)
        }
    }

    //===Desc:=====================================================================================

    override fun onObserve() {
        super.onObserve()
        vm?.apply {
            launchOnUI {
                uiState.collect {
                    when (it) {
                        is PageStatus.Loading -> showLoading()
                        is PageStatus.Success -> showSuccess()
                        is PageStatus.Empty -> showEmpty(it.throwable)
                        is PageStatus.Error -> showEmpty(it.throwable)
                        is PageStatus.Refresh -> refresh()
                        is PageStatus.RefreshSuccess -> refreshSuccess()
                        is PageStatus.RefreshFail -> refreshFail(it.throwable)
                        is PageStatus.LoadMore -> loadMore()
                        is PageStatus.LoadMoreComplete -> loadMoreComplete()
                        is PageStatus.LoadMoreEnd -> loadMoreEnd()
                        is PageStatus.LoadMoreFail -> loadMoreFail()
                        is PageStatus.Progress -> showProgress()
                        is PageStatus.HideProgress -> hideProgress()
                        is PageStatus.Toast -> toast(it.msg, it.isLong)
                        is PageStatus.ToastRes -> toastRes(it.stringId, it.isLong)
                    }
                }
            }
        }
    }

    //===Desc:=====================================================================================

    override fun showLoading() {
        if (null == mLoadingVb) {
            return
        }
        mLoadingVb?.root?.visibility = View.VISIBLE
        vb?.root?.visibility = View.GONE
        mEmptyVb?.root?.visibility = View.GONE
        mErrorVb?.root?.visibility = View.GONE
    }

    override fun showSuccess() {
        if (null == vb) {
            return
        }
        mLoadingVb?.root?.visibility = View.GONE
        vb?.root?.visibility = View.VISIBLE
        mEmptyVb?.root?.visibility = View.GONE
        mErrorVb?.root?.visibility = View.GONE
    }

    override fun showEmpty(throwable: ResultThrowable?) {
        if (null == mEmptyVb) {
            return
        }
        mLoadingVb?.root?.visibility = View.GONE
        vb?.root?.visibility = View.GONE
        mEmptyVb?.root?.visibility = View.VISIBLE
        mErrorVb?.root?.visibility = View.GONE
    }

    override fun showError(throwable: ResultThrowable?) {
        if (null == mErrorVb) {
            return
        }
        mLoadingVb?.root?.visibility = View.GONE
        vb?.root?.visibility = View.GONE
        mEmptyVb?.root?.visibility = View.GONE
        mErrorVb?.root?.visibility = View.VISIBLE
    }

    override fun refresh() {
    }

    override fun refreshSuccess() {
    }

    override fun refreshFail(throwable: ResultThrowable?) {
    }

    override fun loadMore() {
    }

    override fun loadMoreComplete() {
    }

    override fun loadMoreEnd() {
    }

    override fun loadMoreFail(data: Throwable?) {
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun toast(msg: String, isLong: Boolean) {
        mToast.setText(msg)
        mToast.duration = if (isLong) {
            Toast.LENGTH_LONG
        } else {
            Toast.LENGTH_SHORT
        }
        mToast.show()
    }

    override fun toastRes(stringId: Int, isLong: Boolean) {
        toast(getString(stringId), isLong)
    }

    //===Desc:=====================================================================================
    /**隐藏Actionbar*/
    private fun hideActionbar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    /**获取ViewModel*/
    protected fun <T : ViewModel> getCacheViewModel(modelClass: Class<T>): T =
        ViewModelProvider(this)[modelClass]


    //===Desc:提供给子类方法=====================================================================================

    /**```
     * transparentStatusBar                     //透明状态栏，不写默认透明色
     * transparentNavigationBar()                   //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
     * transparentBar()                             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
     * statusBarColor(R.color.colorPrimary)         //状态栏颜色，不写默认透明色
     * navigationBarColor(R.color.colorPrimary)     //导航栏颜色，不写默认黑色
     * barColor(R.color.colorPrimary)               //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
     * statusBarAlpha(0.3f)                         //状态栏透明度，不写默认0.0f
     * navigationBarAlpha(0.4f)                     //导航栏透明度，不写默认0.0F
     * barAlpha(0.3f)                               //状态栏和导航栏透明度，不写默认0.0f
     * statusBarDarkFont(true)                      //状态栏字体是深色，不写默认为亮色
     * navigationBarDarkIcon(true)                  //导航栏图标是深色，不写默认为亮色
     * autoDarkModeEnable(true)                     //自动状态栏字体和导航栏图标变色，必须指定状态栏颜色和导航栏颜色才可以自动变色哦
     * autoStatusBarDarkModeEnable(true,0.2f)       //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
     * autoNavigationBarDarkModeEnable(true,0.2f)   //自动导航栏图标变色，必须指定导航栏颜色才可以自动变色哦
     * flymeOSStatusBarFontColor(R.color.btn3)      //修改flyme OS状态栏字体颜色
     * fullScreen(true)                             //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
     * hideBar(BarHide.FLAG_HIDE_BAR)               //隐藏状态栏或导航栏或两者，不写默认不隐藏
     * addViewSupportTransformColor(toolbar)        //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
     * titleBar(view)                               //解决状态栏和布局重叠问题，任选其一
     * titleBarMarginTop(view)                      //解决状态栏和布局重叠问题，任选其一
     * statusBarView(view)                          //解决状态栏和布局重叠问题，任选其一
     * fitsSystemWindows(true)                      //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
     * supportActionBar(true)                       //支持ActionBar使用
     * statusBarColorTransform(R.color.orange)      //状态栏变色后的颜色
     * navigationBarColorTransform(R.color.orange)  //导航栏变色后的颜色
     * barColorTransform(R.color.orange)            //状态栏和导航栏变色后的颜色
     * removeSupportView(toolbar)                   //移除指定view支持
     * removeSupportAllView()                       //移除全部view支持
     * navigationBarEnable(true)                    //是否可以修改导航栏颜色，默认为true
     * navigationBarWithKitkatEnable(true)          //是否可以修改安卓4.4和emui3.x手机导航栏颜色，默认为true
     * navigationBarWithEMUI3Enable(true)           //是否可以修改emui3.x手机导航栏颜色，默认为true
     * keyboardEnable(true)                         //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
     * keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)    //单独指定软键盘模式
     * setOnKeyboardListener(new OnKeyboardListener() {                     //软键盘监听回调，keyboardEnable为true才会回调此方法
     *      @Override
     *      public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
     *          LogUtils.e(isPopup);  //isPopup为true，软键盘弹出，为false，软键盘关闭
     *      }
     * })
     * setOnNavigationBarListener(onNavigationBarListener)          //导航栏显示隐藏监听，目前只支持华为和小米手机
     * setOnBarListener(OnBarListener)                              //第一次调用和横竖屏切换都会触发，可以用来做刘海屏遮挡布局控件的问题
     * addTag("tag")            //给以上设置的参数打标记
     * getTag("tag")            //根据tag获得沉浸式参数
     * reset()                  //重置所以沉浸式参数
     * init();                  //必须调用方可应用以上所配置的参数  ```
     */
    protected fun setupStatusBar(isOnly: Boolean = false, block: ImmersionBar.() -> Unit) {
        immersionBar(isOnly, block)
    }

    /**
     * 显示或隐藏StatusBar
     */
    protected fun showStatusBar(show: Boolean = true) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val root = vb?.root ?: return
        val windowInsetsController = ViewCompat.getWindowInsetsController(root)
        if (show) {
            windowInsetsController?.show(WindowInsetsCompat.Type.statusBars())
        } else {
            windowInsetsController?.hide(WindowInsetsCompat.Type.statusBars())
        }
    }

    //===Desc:界面显示Fragment相关=====================================================================================
    protected fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(getContentRoot().id, fragment)
            .commitNowAllowingStateLoss()
    }


    //===Desc:状态栏相关=====================================================================================


    //===Desc:Toolbar相关=====================================================================================
    protected fun setupToolbar(block: Toolbar.() -> Unit) {
        mToolbar?.apply {
            block(this)
        }
    }

    protected fun setToolbarLeft(@DrawableRes imgRes: Int, click: View.OnClickListener? = null) {

        mToolbar?.setNavigationIcon(imgRes)
        if (null != click) {
            mToolbar?.setNavigationOnClickListener(click)
        }
    }

    //===Desc:界面相关=====================================================================================
    /**获取Toolbar容器*/
    protected fun getToolbarRoot(): FrameLayout = mToolbarContainer

    /**获取正文容器，包含loading,success,empty,error*/
    protected fun getContentRoot(): FrameLayout = mContentContainer


}