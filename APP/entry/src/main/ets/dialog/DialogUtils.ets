import Context from '@ohos.app.ability.common';

/**
 * 对话框工具类
 *
 * This is a pop-up window tool class, which is used to encapsulate dialog code.
 * Developers can directly invoke the methods in.
 */
class DialogUtils {
  /**
   * Alert dialog dialog
   *
   * 调用方式：
   * let context = getContext(this) as Context.UIAbilityContext;
   * DialogUtils.alertDialog(context, "hello")
   *
   */
  alertDialog(context: Context.UIAbilityContext, msg: string, cancel: string = '取消',  confirm: string = '确定', alignment: DialogAlignment = DialogAlignment.Center, dx: number = 0, dy: number = -20) {
    AlertDialog.show({
      message: msg,
      alignment: alignment,
      offset: {
        dx: dx,
        dy: dy
      },
      primaryButton: {
        value: cancel,
        action: ()=>{
          context.terminateSelf();
        }
      },
      secondaryButton: {
        value: confirm,
        action: () => {
          // Exiting the app.
          context.terminateSelf();
        }
      }
    });
  }


  /**


   /**
   * Date dialog dialog
   *
   * 调用方式：
   * DialogUtils.datePickerDialog(date => {
   *   Logger.error(">>>>>>>>>>>>>>>>>>" + date)
   * })
   */
  datePickerDialog(dateCallback: Function, startTime: string = '1900-1-1', selectTime: string = "1990-1-1") {
    DatePickerDialog.show({
      start: new Date(startTime),
      end: new Date(),
      selected: new Date(selectTime),
      lunar: false,
      onAccept: (value: DatePickerResult) => {
        let year: number = Number(value.year);
        let month: number = Number(value.month) + 1;
        let day: number = Number(value.day);
        let birthdate: string = this.getBirthDateValue(year, month, day);
        dateCallback(birthdate);
      }
    });
  }

  /**
   * Text dialog dialog
   *
   * 调用方式：
   * array: Array<string> = ['aaa', 'bbb', 'ccc'];
   * DialogUtils.textPickerDialog(this.array, res => {
   *   Logger.error(">>>>>>>>>>>>>>>>>>>" + res);
   * })
   */
  textPickerDialog(array: Array<string>, Callback: Function) {
    if (this.isEmpty(array)) {
      return;
    }
    TextPickerDialog.show({
      range: array,
      selected: 0,
      onAccept: (result: TextPickerResult) => {
        Callback(result.value);
      },
      onCancel: () => {
      }
    });
  }

  /**
   * Get default birth date
   *
   * @param {number} year year
   * @param {number} month month
   * @param {number} day day
   * @returns {string} return birth date
   */
  getBirthDateValue(year: number, month: number, day: number): string {
    let birthdate: string = year.toString() + (month < 0 ? ("0" + month) : month) + (day < 0 ? ('0' + day) : day);
    return birthdate;
  }

  /**
   * Check obj is empty
   *
   * @param {object} obj
   * @return {boolean} true(empty)
   */
  isEmpty(obj: object | string): boolean {
    return obj === undefined || obj === null || obj === '';
  }


  /**
   * Check array is empty
   *
   * @param {Array}array
   * @return {boolean} true(empty)
   */
  isEmptyArr(array: Array<Object>): boolean {
    return this.isEmpty(array) || array.length === 0;
  }

}

export default new DialogUtils();